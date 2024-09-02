package com.app.msc_project_administrator.project;

import com.app.msc_project_administrator.user.SupervisorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
