package com.app.msc_project_administrator.supervisorStudentPreference;

import com.app.msc_project_administrator.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWithRankedStudentsDTO {
    private Integer projectId;
    private String title;
    private String description;
    private List<RankedStudentDTO> rankedStudents;

    public ProjectWithRankedStudentsDTO(Project project, List<RankedStudentDTO> rankedStudents) {
        this.projectId = project.getProjectId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.rankedStudents = rankedStudents;
    }

    // Getters and Setters
}