package com.app.msc_project_administrator.supervisorStudentPreference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWithRankedStudentsDTO {
    private Integer projectId;
    private String projectTitle;
    private List<RankedStudentDTO> rankedStudents;

    // Getters and Setters
}
