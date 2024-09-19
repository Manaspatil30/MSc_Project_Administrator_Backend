package com.app.msc_project_administrator.projectAssesment;

import com.app.msc_project_administrator.project.Project;
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
public class ProjectAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assessmentId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private User assessor;  // Supervisor who will assess this project

    private String status; // e.g., "Pending", "Completed"
}
