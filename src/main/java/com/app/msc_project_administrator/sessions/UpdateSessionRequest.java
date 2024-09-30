package com.app.msc_project_administrator.sessions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSessionRequest {
    private String title;
    private LocalDateTime dateTime;
    private String meetingLink;
}
