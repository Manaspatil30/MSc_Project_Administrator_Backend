package com.app.msc_project_administrator.studentChoices;

import com.app.msc_project_administrator.project.Project;
import com.app.msc_project_administrator.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudentChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choiceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User student;

    @ManyToMany
    @JoinTable(
            name = "student_project_choices",
            joinColumns = @JoinColumn(name = "choice_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects;
}
