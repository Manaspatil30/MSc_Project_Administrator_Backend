package com.app.msc_project_administrator.studentChoices;

import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserRepository;
import com.app.msc_project_administrator.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student-choices")
public class StudentChoiceController {

    @Autowired
    private StudentChoiceService studentChoiceService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> saveStudentChoice(Principal principal, @RequestBody StudentChoiceRequest studentChoiceRequest) {
        try {
            User student = userRepository.findByEmail(principal.getName()).orElseThrow();
            studentChoiceService.saveStudentChoice2(student, studentChoiceRequest.getProjectPreferences());
            return ResponseEntity.accepted().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/preferences/{userId}")
    public ResponseEntity<List<ProjectPreferenceResponse>> getStudentProjectPreferences(@PathVariable Long userId) {
        List<ProjectPreferenceResponse> preferences = studentChoiceService.getStudentProjectPreferences(userId);
        return ResponseEntity.ok(preferences);
    }

    @GetMapping("/all-preferences")
    public ResponseEntity<List<Map<String, Object>>> getAllStudentProjectPreferences() {
        List<Map<String, Object>> allPreferences = studentChoiceService.getAllStudentProjectPreferences();
        return ResponseEntity.ok(allPreferences);
    }

    @GetMapping("/project-or-choices/{studentId}")
    public ResponseEntity<?> getProjectOrChoices(@PathVariable Long studentId) {
        Object response = studentChoiceService.getProjectOrChoices(studentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mod-owner/preferences-status")
    public ResponseEntity<Map<String, Object>> getStudentPreferencesStatus() {
        Map<String, Object> response = studentChoiceService.getStudentPreferencesAndMissingSubmissions();
        return ResponseEntity.ok(response);
    }
}
