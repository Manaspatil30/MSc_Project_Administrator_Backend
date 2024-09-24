package com.app.msc_project_administrator.userProjectAssign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAllocationDTO {
    private Long studentId;
    private Long projectId;
    private String studentName;
    private String projectTitle;
}
