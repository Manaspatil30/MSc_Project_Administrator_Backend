package com.app.msc_project_administrator.answer;

import com.app.msc_project_administrator.projectQuestions.ProjectQuestion;
import com.app.msc_project_administrator.studentChoices.StudentChoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_choice_id")
    private StudentChoice studentChoice;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private ProjectQuestion question;

    @Column(nullable = false)
    private String answer;
}
