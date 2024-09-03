package com.app.msc_project_administrator.user;


import com.app.msc_project_administrator.project.ProjectDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private ProjectDTO assignedProject;
}
