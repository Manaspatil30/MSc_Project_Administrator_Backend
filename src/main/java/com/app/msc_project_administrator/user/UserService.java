package com.app.msc_project_administrator.user;

import com.app.msc_project_administrator.project.Project;
import com.app.msc_project_administrator.project.ProjectDTO;
import com.app.msc_project_administrator.project.ProjectRepository;
import com.app.msc_project_administrator.userProjectAssign.UserProjectAssignment;
import com.app.msc_project_administrator.userProjectAssign.UserProjectAssignmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final UserProjectAssignmentRepository userProjectAssignmentRepository;
    private final ProjectRepository projectRepository;
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    public List<User> findAll(){
        return repository.findAll();
    }

    public List<User> getUsersByIds(List<Integer> ids){
        return repository.findAllByUserIdIn(ids);
    }

    public List<User> getAllSupervisors(Role role) {return repository.findByRole(role);}

    public List<UserDTO> getStudentsBySupervisor(Long supervisorId) {
        List<User> students = repository.findStudentsBySupervisorId(supervisorId);
        return students.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }



    public List<UserDTO> getStudentsAndAssignedProjects(Long supervisorId) {
        List<UserProjectAssignment> assignments = userProjectAssignmentRepository.findBySupervisorId(supervisorId);

        return assignments.stream().map(assignment -> {
            User student = assignment.getUser();
            Project project = assignment.getProject();

            // Map to UserDTO and include project details
            UserDTO studentDTO = mapToUserDTO(student);
            ProjectDTO projectDTO = mapToProjectDTO(project);
            studentDTO.setAssignedProject(projectDTO);

            return studentDTO;
        }).collect(Collectors.toList());
    }

    private ProjectDTO mapToProjectDTO(Project project) {
        return new ProjectDTO(
                project.getProjectId(),
                project.getSupProjectId(),
                project.getTitle(),
                project.getDescription(),
                project.getStatus(),
                null, // Add SupervisorDTO if needed
                project.getPrograme() // assuming `getPrograme` gives related tags or programs
        );
    }

    public UserDTO mapToUserDTO(User user) {

        Optional<UserProjectAssignment> assignment = userProjectAssignmentRepository.findByUser_UserId(user.getUserId());

        ProjectDTO projectDTO = null;
        if (assignment.isPresent()) {
            Project assignedProject = assignment.get().getProject();

        SupervisorDTO supervisorDTO = null;
        if (assignedProject.getSupervisor() != null) {
            supervisorDTO = new SupervisorDTO(
                    assignedProject.getSupervisor().getUserId(),
                    assignedProject.getSupervisor().getFirstname(),
                    assignedProject.getSupervisor().getLastname(),
                    assignedProject.getSupervisor().getEmail()
            );
        }


            projectDTO = new ProjectDTO(
                    assignedProject.getProjectId(),
                    assignedProject.getSupProjectId(),
                    assignedProject.getTitle(),
                    assignedProject.getDescription(),
                    assignedProject.getStatus(),
                    supervisorDTO,
                    assignedProject.getPrograme()
            );
        }

        return new UserDTO(
                user.getUserId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole(),
                projectDTO
        );
    }

}
