package com.app.msc_project_administrator.studentChoices;

import com.app.msc_project_administrator.answer.AnswerDTO;
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
        private List<AnswerDTO> answers;
    }
}
