package com.app.msc_project_administrator.project;

import com.app.msc_project_administrator.modOwner.ModProjectDTO;
import com.app.msc_project_administrator.modOwner.ModUserDTO;
import com.app.msc_project_administrator.programe.Programe;
import com.app.msc_project_administrator.programe.ProgrameRepository;
import com.app.msc_project_administrator.projectAssesment.ProjectAssessment;
import com.app.msc_project_administrator.projectAssesment.ProjectAssessmentRepository;
import com.app.msc_project_administrator.projectQuestions.ProjectQuestion;
import com.app.msc_project_administrator.user.*;
import com.app.msc_project_administrator.userProjectAssign.UserProjectAssignment;
import com.app.msc_project_administrator.userProjectAssign.UserProjectAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository repository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ProgrameRepository programeRepository;
    private final UserProjectAssignmentRepository userProjectAssignmentRepository;
    private final ProjectAssessmentRepository projectAssessmentRepository;
    public Project createProject(ProjectRequest request){
        Set<Programe> programs = new HashSet<>(programeRepository.findAllById(request.getProgrameIds()));
        //Get Supervisor
        Optional<User> optionalSupervisor = userRepository.findById(request.getSupervisor());
        if (!optionalSupervisor.isPresent()) {
            throw new RuntimeException("Supervisor not found");
        }
        User supervisor = optionalSupervisor.get();

        //Generate Project Id based on supervisors initials and sequence number
        String supervisorInitials = supervisor.getFirstname().substring(0,1) + supervisor.getLastname().substring(0,1);
        long projectCount = repository.countBySupervisor(supervisor);
        String supProjectId = supervisorInitials + (projectCount + 1);

        // Create Project Object
        Project project = new Project();
        project.setSupProjectId(supProjectId);
        project.setTitle(request.getTitle());
        project.setSupervisor(supervisor);
        project.setPrograme(programs);
        project.setStatus(request.getStatus());
        project.setDescription(request.getDescription());
        project.setPrerequisite(request.getPrerequisite());
        project.setSuitableFor(request.getSuitableFor());
        project.setQuota(request.getQuota());
        project.setReference(request.getReference());


        // Handle multiple associate supervisors using getAssociateSupervisorIds method
        if (request.getAssociateSupervisorIds() != null && !request.getAssociateSupervisorIds().isEmpty()) {
            List<User> associateSupervisors = userRepository.findAllByUserIdIn(request.getAssociateSupervisorIds());
            project.setAssociateSupervisors(new HashSet<>(associateSupervisors));
        }

        //Set Tags
        Set<Tag> tags = new HashSet<>();
        for (String tagName : request.getTags()){
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));
            tags.add(tag);
        }
        project.setTags(tags);

        //Set Questions
        List<ProjectQuestion> questions = request.getQuestions();
        for (ProjectQuestion question : questions) {
            question.setProject(project);
        }

        project.setQuestions(questions);

        //Save project
        return repository.save(project);
    }



    public List<ProjectDTO> findAll(){
        List<Project> projects = repository.findAll();
        return projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Project getProjectById(Long projectId){
        return repository.findProjectByProjectId(projectId);
    }

    public List<Project> getProjectBySupervisor(User supervisor){
        return repository.findAllBySupervisor(supervisor);
    }

    public ProjectDTO getAssignedProject(Long studentId) {
        Optional<UserProjectAssignment> student = userProjectAssignmentRepository.findByUser_UserId(studentId);
        if (student.isPresent()) {
            Project assignedProject = student.get().getProject();
            if (assignedProject != null) {
                SupervisorDTO supervisorDTO = new SupervisorDTO(
                        assignedProject.getSupervisor().getUserId(),
                        assignedProject.getSupervisor().getFirstname(),
                        assignedProject.getSupervisor().getLastname(),
                        assignedProject.getSupervisor().getEmail()
                );

                return new ProjectDTO(
                        assignedProject.getProjectId(),
                        assignedProject.getSupProjectId(),
                        assignedProject.getTitle(),
                        assignedProject.getDescription(),
                        assignedProject.getStatus(),
                        supervisorDTO,
                        assignedProject.getPrograme(),
                        null,
                        assignedProject.getQuota()
                );
            } else {
                throw new RuntimeException("No project assigned to this student.");
            }
        } else {
            throw new RuntimeException("Student not found.");
        }
    }

    public List<ModProjectDTO> getAssignedProjectsWithoutAssessors(User modOwner) {
        // Ensure the user has MOD_OWNER role
        if (!modOwner.getRole().equals(Role.MOD_OWNER)) {
            throw new RuntimeException("Only module owners can view assigned projects.");
        }

        // Fetch all projects assigned to students
        List<UserProjectAssignment> assignments = userProjectAssignmentRepository.findAll();

        // Fetch all project IDs that have assessors
        List<Integer> projectsWithAssessors = projectAssessmentRepository.findAll().stream()
                .map(assessment -> assessment.getProject().getProjectId())
                .collect(Collectors.toList());

        // Filter projects that do not have an assessor
        return assignments.stream()
                .filter(assignment -> !projectsWithAssessors.contains(assignment.getProject().getProjectId()))
                .map(assignment -> {
                    Project project = assignment.getProject();
                    User student = assignment.getUser();
                    User supervisor = project.getSupervisor();

                    // Convert student to UserDTO
                    ModUserDTO studentDTO = new ModUserDTO(
                            student.getUserId(),
                            student.getFirstname(),
                            student.getLastname(),
                            student.getEmail()
                    );

                    // Convert supervisor to UserDTO
                    ModUserDTO supervisorDTO = supervisor != null ? new ModUserDTO(
                            supervisor.getUserId(),
                            supervisor.getFirstname(),
                            supervisor.getLastname(),
                            supervisor.getEmail()
                    ) : null;

                    // Convert project to ProjectDTO
                    return new ModProjectDTO(
                            project.getProjectId(),
                            project.getTitle(),
                            project.getDescription(),
                            project.getStatus(),
                            List.of(studentDTO),
                            supervisorDTO
                    );
                })
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getProjectsForSupervisor(Long supervisorId) {
        // Fetch all projects supervised by the given supervisor ID
        List<Project> projects = repository.findAllBySupervisorUserId(supervisorId);

        // Map the projects to ProjectDTO
        return projects.stream()
                .map(project -> new ProjectDTO(
                        project.getProjectId(),
                        project.getSupProjectId(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getStatus(),
                        null,
                        null,
                        project.getQuestions(),
                        project.getQuota()

                ))
                .collect(Collectors.toList());
    }


    public List<ModProjectDTO> getProjectsWithAssessors(User modOwner) {
        // Ensure the user has MOD_OWNER role
        if (!modOwner.getRole().equals(Role.MOD_OWNER)) {
            throw new RuntimeException("Only module owners can view all projects with assessors.");
        }

        // Fetch all project assessments
        List<ProjectAssessment> assessments = projectAssessmentRepository.findAll();

        // Collect projects and associated students with assessors
        return assessments.stream()
                .map(assessment -> {
                    Project project = assessment.getProject();
                    User supervisor = assessment.getAssessor();


                    // Convert supervisor to UserDTO
                    ModUserDTO supervisorDTO = new ModUserDTO(
                            supervisor.getUserId(),
                            supervisor.getFirstname(),
                            supervisor.getLastname(),
                            supervisor.getEmail()
                    );

                    // Convert project to ProjectDTO
                    return new ModProjectDTO(
                            project.getProjectId(),
                            project.getTitle(),
                            project.getDescription(),
                            project.getStatus(),
                            null,
                            supervisorDTO
                    );
                })
                .collect(Collectors.toList());
    }


    public List<UserDTO> getSupervisorsByExpertise(Long projectId) {
        // Fetch the project based on projectId
        Project project = repository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Get the supervisor of the project
        User projectSupervisor = project.getSupervisor();

        // Fetch the tags (expertise) of the project
        Set<Tag> projectTags = project.getTags();

        // Step 1: Find supervisors who have expertise related to the project's tags, excluding the project's supervisor
        List<User> relevantSupervisors = userRepository.findSupervisorsByTags(projectTags).stream()
                .filter(supervisor -> !supervisor.getUserId().equals(projectSupervisor.getUserId()))
                .collect(Collectors.toList());

        // Step 2: Find all other supervisors who are not as relevant, excluding the project's supervisor
        List<User> otherSupervisors = userRepository.findAllSupervisorsExcludingTags(projectTags).stream()
                .filter(supervisor -> !supervisor.getUserId().equals(projectSupervisor.getUserId()))
                .collect(Collectors.toList());

        // Convert to UserDTO (supervisors with expertise first, then the rest)
        List<UserDTO> supervisorDTOs = new ArrayList<>();

        // Convert relevant supervisors
        relevantSupervisors.forEach(supervisor -> supervisorDTOs.add(new UserDTO(
                supervisor.getUserId(),
                supervisor.getFirstname(),
                supervisor.getLastname(),
                supervisor.getEmail(),
                supervisor.getRole(),
                null
        )));

        // Convert other supervisors
        otherSupervisors.forEach(supervisor -> supervisorDTOs.add(new UserDTO(
                supervisor.getUserId(),
                supervisor.getFirstname(),
                supervisor.getLastname(),
                supervisor.getEmail(),
                supervisor.getRole(),
                null
        )));

        return supervisorDTOs;
    }

    public void assignProjectToStudent(Long studentId, Long projectId) {
        // Fetch student and project
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Project project = repository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // Check if student already has a project assigned
        Optional<UserProjectAssignment> existingAssignment = userProjectAssignmentRepository.findByUser(student);
        if (existingAssignment.isPresent()) {
            throw new IllegalStateException("Student already has an assigned project.");
        }

        // Create new assignment
        UserProjectAssignment assignment = new UserProjectAssignment();
        assignment.setUser(student);
        assignment.setProject(project);

        // Save assignment
        userProjectAssignmentRepository.save(assignment);
    }


    public List<ProjectDTO> filterProjects(String programIds, String tagNames, String supervisorName, String title) {
        List<Long> programIdList = programIds != null ? Arrays.stream(programIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList()) : null;

        List<String> tagNameList = tagNames != null ? Arrays.asList(tagNames.split(",")) : null;

        List<Project> projects = repository.filterProjects(programIdList, tagNameList, supervisorName, title);

        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Find all projects and return them sorted by supProjectId
    public List<ProjectDTO> findAllSortedBySupProjectId() {
        List<Project> projects = repository.findAllSortedBySupProjectId();
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ProjectDTO convertToDTO(Project project) {
        SupervisorDTO supervisorDTO = null;
        if (project.getSupervisor() != null) {
            supervisorDTO = new SupervisorDTO(
                    project.getSupervisor().getUserId(),
                    project.getSupervisor().getFirstname(),
                    project.getSupervisor().getLastname(),
                    project.getSupervisor().getEmail()
            );
        }

        return new ProjectDTO(
                project.getProjectId(),
                project.getSupProjectId(),
                project.getTitle(),
                project.getDescription(),
                project.getStatus(),
                supervisorDTO,
                project.getPrograme(),
                project.getQuestions(),
                project.getQuota()
        );
    }

    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

}
