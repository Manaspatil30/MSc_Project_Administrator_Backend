package com.app.msc_project_administrator.projectAssesment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssignmentRequest {
    private Long projectId;
    private Long supervisorId;
}
