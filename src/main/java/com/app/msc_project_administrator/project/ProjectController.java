package com.app.msc_project_administrator.project;

import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    @Autowired
    UserRepository userRepository;

    private final ProjectService service;

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
    public ResponseEntity<List<Project>> getAllProjects(){
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
}
