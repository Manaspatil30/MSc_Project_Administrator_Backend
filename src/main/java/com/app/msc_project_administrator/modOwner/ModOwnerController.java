package com.app.msc_project_administrator.modOwner;

import com.app.msc_project_administrator.project.ProjectDTO;
import com.app.msc_project_administrator.project.ProjectService;
import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/modowner")
@RequiredArgsConstructor
public class ModOwnerController {
    private final ProjectService projectService;

    @GetMapping("/assignedProjects")
    public ResponseEntity<List<ModProjectDTO>> getAssignedProjects(Principal principal) {
        // Fetch MOD_OWNER details from the principal
        User modOwner = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        // Fetch all assigned projects
        List<ModProjectDTO> assignedProjects = projectService.getAllAssignedProjectsForModOwner(modOwner);

        return ResponseEntity.ok(assignedProjects);
    }

    @GetMapping("/supervisors")
    public ResponseEntity<List<UserDTO>> getSupervisorsByExpertise(@RequestParam Long projectId) {
        // Fetch supervisors based on expertise for the provided project
        List<UserDTO> supervisors = projectService.getSupervisorsByExpertise(projectId);
        return ResponseEntity.ok(supervisors);
    }
}
