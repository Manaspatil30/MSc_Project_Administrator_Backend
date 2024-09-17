package com.app.msc_project_administrator.user;

import com.app.msc_project_administrator.studentChoices.StudentChoiceService;
import com.app.msc_project_administrator.supervisorStudentPreference.ProjectWithRankedStudentsDTO;
import com.app.msc_project_administrator.supervisorStudentPreference.SupervisorStudentPreference;
import com.app.msc_project_administrator.supervisorStudentPreference.SupervisorStudentPreferenceDTO;
import com.app.msc_project_administrator.supervisorStudentPreference.SupervisorStudentPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final SupervisorStudentPreferenceService supervisorStudentPreferenceService;
    private final StudentChoiceService studentChoiceService;
    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

   @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(service.findAll());
   }

   @GetMapping("/by-id")
   @PreAuthorize("#id.contains(#user.userId)")
    public ResponseEntity<List<User>> getAllUsersById(@RequestParam List<Integer> id, @AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.getUsersByIds(id));
   }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(@AuthenticationPrincipal User user) {
        // Use the service to map the user entity to UserDTO
        UserDTO userDTO = service.mapToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

   @GetMapping("/supervisor")
    public ResponseEntity<List<User>> getSupervisors(@RequestParam Role role) {
        return  ResponseEntity.ok(service.getAllSupervisors(role));
   }

//    @GetMapping("/supervisor/{supervisorId}/students")
//    public ResponseEntity<List<UserDTO>> getStudentsBySupervisor(@PathVariable Long supervisorId) {
//        List<UserDTO> students = service.getStudentsBySupervisor(supervisorId);
//        return ResponseEntity.ok(students);
//    }

    @GetMapping("/{supervisorId}/students")
    public List<UserDTO> getStudentsAndAssignedProjects(@PathVariable Long supervisorId) {
        return service.getStudentsAndAssignedProjects(supervisorId);
    }

    @PostMapping("/supervisor/{supervisorId}/rank-students")
    public ResponseEntity<?> rankStudents(@PathVariable Long supervisorId, @RequestParam Integer projectId, @RequestBody List<SupervisorStudentPreferenceDTO> preferences) {
        supervisorStudentPreferenceService.savePreferences(supervisorId, projectId, preferences);
        return ResponseEntity.ok("Preferences saved successfully.");
    }

    @GetMapping("/{supervisorId}/students-grouped-by-project")
    public List<ProjectWithStudentsDTO> getStudentsGroupedByProject(@PathVariable Long supervisorId) {
        return service.getStudentsForSupervisor(supervisorId);
    }

    @GetMapping("/projects-with-ranked-students")
    public ResponseEntity<List<ProjectWithRankedStudentsDTO>> getProjectsWithRankedStudents() {
        return ResponseEntity.ok(service.getProjectsWithRankedStudents());
    }
}
