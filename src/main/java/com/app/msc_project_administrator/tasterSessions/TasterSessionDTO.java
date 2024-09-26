package com.app.msc_project_administrator.tasterSessions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TasterSessionDTO {
    private Long sessionId;
    private Long supervisorId;
    private String supervisorName;
    private Long studentId;
    private String studentName;
    private String meetingLink;
    private String description;
    private LocalDateTime startTime;
    private TasterSessionStatus status;
}
