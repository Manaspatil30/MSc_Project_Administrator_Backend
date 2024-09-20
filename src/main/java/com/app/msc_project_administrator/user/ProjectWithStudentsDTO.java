package com.app.msc_project_administrator.user;

import com.app.msc_project_administrator.answer.UserWithAnswersDTO;
import com.app.msc_project_administrator.project.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWithStudentsDTO {
    private Integer projectId;
    private String supProjectId;
    private String title;
    private String description;
    private Status status;
    private List<UserWithAnswersDTO> students;

    // Constructor, getters, and setters
}

