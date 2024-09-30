package com.app.msc_project_administrator.project;

import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserRepository;
import com.app.msc_project_administrator.userProjectAssign.ProjectAssignmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ProjectService service;

    @Autowired
    private ProjectRepository projectRepository;


    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody ProjectRequest request){
        try{
            service.createProject(request);
            return ResponseEntity.accepted().build();
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = service.findAllSortedBySupProjectId();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/supervisor/{supervisorId}/projects")
    public ResponseEntity<List<ProjectDTO>> getProjectsForSupervisor(@PathVariable Long supervisorId) {
        List<ProjectDTO> projects = service.getProjectsForSupervisor(supervisorId);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignProjectToStudent(@RequestBody ProjectAssignmentRequest assignmentRequest) {
        try {
            service.assignProjectToStudent(assignmentRequest.getStudentId(), assignmentRequest.getProjectId());
            return ResponseEntity.ok("Project assigned successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while assigning the project.");
        }
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@RequestParam Long projectId){
        return ResponseEntity.ok(service.getProjectById(projectId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Project>> getProjectsBySupervisor(@RequestParam User supervisor){
        return ResponseEntity.ok(service.getProjectBySupervisor(supervisor));
    }

    @GetMapping("/assigned-project")
    public ResponseEntity<?> getAssignedProject(Principal principal) {
        Optional<User> student = userRepository.findByEmail(principal.getName());
        if (student.isPresent()) {
            try {
                ProjectDTO assignedProject = service.getAssignedProject(student.get().getUserId());
                return ResponseEntity.ok(assignedProject);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(404).body("Student not found");
        }
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagDTO>> getAllTags() {
        List<TagDTO> tags = service.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProjectDTO>> filterProjects(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String programIds,
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String supervisorName
    ) {
        List<ProjectDTO> filteredProjects = service.filterProjects( programIds, tagName, supervisorName, title);
        return ResponseEntity.ok(filteredProjects);
    }
}
