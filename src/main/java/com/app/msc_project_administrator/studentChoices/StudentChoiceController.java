package com.app.msc_project_administrator.studentChoices;

import com.app.msc_project_administrator.user.User;
import com.app.msc_project_administrator.user.UserRepository;
import com.app.msc_project_administrator.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/student-choices")
public class StudentChoiceController {

    @Autowired
    private StudentChoiceService studentChoiceService;
    private UserRepository userRepository;

//    @PostMapping
//    public ResponseEntity<?> saveStudentChoice(Principal principal, @RequestBody List<String> projectIds) {
//        try{
//            User student = userRepository.findByEmail(principal.getName()).orElseThrow();
//            studentChoiceService.saveStudentChoice(student, projectIds);
//            return ResponseEntity.accepted().build();
//        } catch (RuntimeException e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}
