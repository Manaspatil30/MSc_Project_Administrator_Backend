package com.app.msc_project_administrator.modOwner;

import com.app.msc_project_administrator.project.Status;
import com.app.msc_project_administrator.user.SupervisorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModProjectDTO {
    private Integer projectId;
    private String title;
    private String description;
    private Status status;
    private List<ModUserDTO> students;
    private ModUserDTO assessor;
}
