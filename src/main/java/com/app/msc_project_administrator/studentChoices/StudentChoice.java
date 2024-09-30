package com.app.msc_project_administrator.studentChoices;

import com.app.msc_project_administrator.answer.Answer;
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

    @ElementCollection
    @CollectionTable(name = "student_choice_preference", joinColumns = @JoinColumn(name = "student_choice_choice_id"))
    @Column(name = "preferences")
    private List<Integer> preferences;

    @OneToMany(mappedBy = "studentChoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;

    @PrePersist
    @PreUpdate
    private void validateChoices(){
        if (projects.size() > 5) {
            throw new IllegalArgumentException("You can select a maximum of 5 projects");
        }
        if (projects.size() != preferences.size()) {
            throw new IllegalArgumentException("Projects and preferences must have the same size");
        }
    }
}
