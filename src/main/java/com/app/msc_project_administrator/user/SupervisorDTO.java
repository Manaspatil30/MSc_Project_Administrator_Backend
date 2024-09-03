package com.app.msc_project_administrator.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorDTO {
    private Long userId;
    private String firstname;
    private String lastname;
    private String email;
}
