package com.app.msc_project_administrator.supervisorStudentPreference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RankedStudentDTO {
    private Long studentId;
    private String studentName;
    private int preference;

    public RankedStudentDTO(Long studentId, String studentName, int preference) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.preference = preference;
    }

    // Getters and Setters
}
