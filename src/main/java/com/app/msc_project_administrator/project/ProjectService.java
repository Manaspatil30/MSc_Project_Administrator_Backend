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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
}
