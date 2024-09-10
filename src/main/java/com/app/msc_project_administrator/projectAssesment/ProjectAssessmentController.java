package com.app.msc_project_administrator.projectAssesment;

import com.app.msc_project_administrator.project.ProjectDTO;
import com.app.msc_project_administrator.project.ProjectService;
import com.app.msc_project_administrator.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/project-assessment")
@RequiredArgsConstructor
public class ProjectAssessmentController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    ProjectAssessmentRepository projectAssessmentRepository;

    @Autowired
    private ProjectAssessmentService projectAssessmentService;
    @PostMapping("/assignProjectForAssessment")
    public ResponseEntity<?> assignProjectForAssessment(@RequestBody AssignmentRequest request) {
        projectAssessmentService.assignProjectForAssessment(request.getProjectId(), request.getSupervisorId());
        return ResponseEntity.ok("Project assigned for assessment");
    }

    @GetMapping("/viewAssignedProjects")
    public List<ProjectDTO> viewAssignedProjects(Principal principal) {
        User supervisor = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        List<ProjectAssessment> assessments = projectAssessmentRepository.findBySupervisor(supervisor);

        return assessments.stream()
                .map(assessment -> projectService.convertToDTO(assessment.getProject()))
                .collect(Collectors.toList());
    }
}
