package com.app.msc_project_administrator.supervisorStudentPreference;


import com.app.msc_project_administrator.user.User;
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
public class SupervisorStudentPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User supervisor;

    @ManyToOne
    private User student;

    private int preference; // Ranking: 1 for highest preference, etc.
}
