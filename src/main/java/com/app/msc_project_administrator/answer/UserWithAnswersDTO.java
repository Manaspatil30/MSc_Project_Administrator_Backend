package com.app.msc_project_administrator.answer;

import com.app.msc_project_administrator.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithAnswersDTO extends UserDTO {
    private List<AnswerDTO> answers;
}
