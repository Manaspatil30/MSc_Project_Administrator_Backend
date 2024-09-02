package com.app.msc_project_administrator.studentChoices;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentChoiceRequest {
    private List<ProjectPreference> projectPreferences;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectPreference {
        private Long projectId;
        private Integer preference;
    }
}
