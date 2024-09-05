package com.app.msc_project_administrator.project;

import com.app.msc_project_administrator.programe.Programe;
import com.app.msc_project_administrator.programe.ProgrameRepository;
import com.app.msc_project_administrator.projectQuestions.ProjectQuestion;
import com.app.msc_project_administrator.user.SupervisorDTO;
import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserRepository;
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

    public Project createProject(ProjectRequest request){
        Set<Programe> programs = new HashSet<>(programeRepository.findAllById(request.getProgrameIds()));
        //Get Supervisor
        Optional<User> optionalSupervisor = userRepository.findById(request.getSupervisor());
        if (!optionalSupervisor.isPresent()) {
            throw new RuntimeException("Supervisor not found");
        }
        User supervisor = optionalSupervisor.get();

        //Get associateSupervisor
//        User associateSupervisor = null;
//        if(request.getAssociateSupervisor() != null) {
//            Optional<User> optionalAssociateSupervisor = userRepository.findById(request.getAssociateSupervisor());
//            if(optionalAssociateSupervisor.isPresent()){
//                associateSupervisor = optionalAssociateSupervisor.get();
//            }
//        }

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

        // Handle multiple associate supervisors
//        Set<User> associateSupervisors = new HashSet<>();
//        if (request.getAssociateSupervisorIds() != null) {
//            for (Integer supervisorId : request.getAssociateSupervisorIds()) {
//                User associateSupervisor = userRepository.findById(supervisorId)
//                        .orElseThrow(() -> new RuntimeException("Associate Supervisor not found"));
//                associateSupervisors.add(associateSupervisor);
//            }
//        }
//        project.setAssociateSupervisors(associateSupervisors);

        // Handle multiple associate supervisors using the new method
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
                        assignedProject.getPrograme()
                );
            } else {
                throw new RuntimeException("No project assigned to this student.");
            }
        } else {
            throw new RuntimeException("Student not found.");
        }
    }

//    public List<ProjectDTO> filterProjects(String title, Long programId, String tagName, Long supervisorId) {
//        List<Project> projects;
//
//        // Search by title
//        if (title != null && !title.isEmpty()) {
//            projects = repository.findByTitleContainingIgnoreCase(title);
//        }
//        // Filter by program
//        else if (programId != null) {
//            projects = repository.findByPrograme(programId);
//        }
//        // Filter by tag
//        else if (tagName != null && !tagName.isEmpty()) {
//            projects = repository.findByTag(tagName);
//        }
//        // Filter by supervisor
//        else if (supervisorId != null) {
//            User supervisor = userRepository.findById(supervisorId).orElseThrow(() -> new RuntimeException("Supervisor not found"));
//            projects = repository.findBySupervisor(supervisor);
//        } else {
//            projects = repository.findAll();
//        }
//
//        // Convert to DTOs and return
//        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
//    }

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

    private ProjectDTO convertToDTO(Project project) {
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
                // Include programs in the DTO
                project.getPrograme()
        );
    }

    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }
}
