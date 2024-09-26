package com.app.msc_project_administrator.sessions;

import com.app.msc_project_administrator.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "supervisor_id", nullable = false)
    private User supervisor;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private String title;
    private LocalDateTime dateTime;
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    private SessionStatus status; // PENDING, CONFIRMED

    // Getters and setters
}
