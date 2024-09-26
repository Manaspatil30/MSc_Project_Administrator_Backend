package com.app.msc_project_administrator.tasterSessions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TasterSessionRequestDTO {
    private Long studentId;
    private Long supervisorId;
    private TasterSessionStatus status;
}
