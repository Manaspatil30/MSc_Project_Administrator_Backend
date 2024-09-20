package com.app.msc_project_administrator.project;

import com.app.msc_project_administrator.programe.Programe;
import com.app.msc_project_administrator.projectQuestions.ProjectQuestion;
import com.app.msc_project_administrator.user.SupervisorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Integer projectId;
    private String supProjectId;
    private String title;
    private String description;
    private Status status;
    private SupervisorDTO supervisor;
    private Set<Programe> programes;
    private List<ProjectQuestion> questions;
}
