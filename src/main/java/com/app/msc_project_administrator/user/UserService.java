package com.app.msc_project_administrator.user;

import com.app.msc_project_administrator.project.ProjectDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
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

    public UserDTO mapToUserDTO(User user) {
        ProjectDTO projectDTO = null;
        if (user.getAssignedProject() != null) {
            projectDTO = new ProjectDTO(
                    user.getAssignedProject().getProjectId(),
                    user.getAssignedProject().getSupProjectId(),
                    user.getAssignedProject().getTitle(),
                    user.getAssignedProject().getDescription(),
                    user.getAssignedProject().getStatus(),
                    null // We exclude nested supervisor and other details
            );
        }

        return new UserDTO(
                user.getUserId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                projectDTO
        );
    }

}
