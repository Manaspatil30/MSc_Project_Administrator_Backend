package com.app.msc_project_administrator.supervisorStudentPreference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankedStudentDTO {
    private Long userId;
    private String firstname;
    private String lastname;
    private String email;
    private int preference;
}
