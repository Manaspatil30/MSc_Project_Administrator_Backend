package com.app.msc_project_administrator.project;

import com.app.msc_project_administrator.projectQuestions.ProjectQuestion;
import com.app.msc_project_administrator.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProjectRequest {
    private Integer projectId;
    private String supProjectId;
    private String title;
    private String description;
    private Integer supervisor;
    private Integer associateSupervisor;
    private String program;
    private Status status;
    private List<ProjectQuestion> questions;

    private String prerequisite;
    private String suitableFor;
    private Integer quota;
    private String reference;
    private List<String> tags;
}
