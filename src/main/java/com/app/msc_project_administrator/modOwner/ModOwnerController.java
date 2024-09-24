package com.app.msc_project_administrator.modOwner;

import com.app.msc_project_administrator.project.ProjectDTO;
import com.app.msc_project_administrator.project.ProjectService;
import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserDTO;
import com.app.msc_project_administrator.userProjectAssign.ProjectAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/modowner")
@RequiredArgsConstructor
public class ModOwnerController {
    private final ProjectService projectService;
    private final ProjectAssignmentService projectAssignmentService;

    @GetMapping("/assignedProjects")
    public ResponseEntity<List<ModProjectDTO>> getAssignedProjects(Principal principal) {
        // Fetch MOD_OWNER details from the principal
        User modOwner = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        // Fetch all assigned projects
        List<ModProjectDTO> assignedProjects = projectService.getAssignedProjectsWithoutAssessors(modOwner);

        return ResponseEntity.ok(assignedProjects);
    }

    @GetMapping("/supervisors")
    public ResponseEntity<List<UserDTO>> getSupervisorsByExpertise(@RequestParam Long projectId) {
        // Fetch supervisors based on expertise for the provided project
        List<UserDTO> supervisors = projectService.getSupervisorsByExpertise(projectId);
        return ResponseEntity.ok(supervisors);
    }

    @GetMapping("/projectsWithAssessors")
    public ResponseEntity<List<ModProjectDTO>> getProjectsWithAssessors(Principal principal) {
        // Fetch MOD_OWNER details from the principal
        User modOwner = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        // Fetch projects with assessors
        List<ModProjectDTO> projectsWithAssessors = projectService.getProjectsWithAssessors(modOwner);

        return ResponseEntity.ok(projectsWithAssessors);
    }

    @PostMapping("/allocate-projects")
    public ResponseEntity<String> allocateProjects() {
        try {
            projectAssignmentService.allocateProjects();
            return ResponseEntity.ok("Projects allocated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during allocation: " + e.getMessage());
        }
    }
}
