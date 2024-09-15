package com.app.msc_project_administrator.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProjectResponse {
    private Long studentId;
    private String studentFirstname;
    private String studentLastname;
    private String studentEmail;
    private Integer projectId;
    private String projectTitle;

}
