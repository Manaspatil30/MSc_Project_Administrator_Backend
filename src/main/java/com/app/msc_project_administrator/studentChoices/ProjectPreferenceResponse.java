package com.app.msc_project_administrator.studentChoices;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPreferenceResponse {
    private Integer studerntId;
    private Integer projectId;
    private String projectTitle;
    private Integer preference;
}
