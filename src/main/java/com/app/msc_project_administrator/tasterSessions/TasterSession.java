package com.app.msc_project_administrator.tasterSessions;

import com.app.msc_project_administrator.project.Project;
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
@Table(name = "taster_sessions")
public class TasterSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tasterSessionId;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private User supervisor;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = true)
    private User student; // Can be null initially

    private String meetingLink;
    private String description;
    private LocalDateTime startTime;

    @Enumerated(EnumType.STRING)
    private TasterSessionStatus status; // Enum type 'AVAILABLE', 'BOOKED' // 'AVAILABLE', 'BOOKED'

    // getters and setters
}
