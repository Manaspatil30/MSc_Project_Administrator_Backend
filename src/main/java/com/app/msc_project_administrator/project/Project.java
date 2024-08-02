package com.app.msc_project_administrator.project;

import com.app.msc_project_administrator.projectQuestions.ProjectQuestion;
import com.app.msc_project_administrator.studentChoices.StudentChoice;
import com.app.msc_project_administrator.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    private String supProjectId;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "supervisor_id", nullable = false)
    private User supervisor;

    @ManyToOne
    @JoinColumn(name = "associate_supervisor_id")
    private User associateSupervisor;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProjectQuestion> questions;

    @ManyToMany(mappedBy = "projects")
    private List<StudentChoice> studentChoices;

    private String program;

    private String reference;
    private String prerequisite;
    private String suitableFor;
    private Integer quota;

    @ElementCollection
    private List<String> tags;

}
