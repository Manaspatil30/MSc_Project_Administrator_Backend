package com.app.msc_project_administrator.supervisorStudentPreference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorStudentPreferenceDTO {
    private Long studentId;
    private int preference;
}
