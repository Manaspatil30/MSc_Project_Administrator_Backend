package com.app.msc_project_administrator.project;

import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserRepository;
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

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody ProjectRequest request){
        try{
            service.createProject(request);
            return ResponseEntity.accepted().build();
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
//        service.save(request);
//        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(){
        return ResponseEntity.ok(service.findAll());
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
}
