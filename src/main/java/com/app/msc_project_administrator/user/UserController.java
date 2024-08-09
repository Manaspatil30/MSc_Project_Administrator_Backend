package com.app.msc_project_administrator.user;

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

   @GetMapping("/supervisor")
    public ResponseEntity<List<User>> getSupervisors(@RequestParam Role role) {
        return  ResponseEntity.ok(service.getAllSupervisors(role));
   }
}
