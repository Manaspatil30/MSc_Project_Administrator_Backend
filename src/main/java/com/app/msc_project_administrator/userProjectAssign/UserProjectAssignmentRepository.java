package com.app.msc_project_administrator.userProjectAssign;

import com.app.msc_project_administrator.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProjectAssignmentRepository extends JpaRepository<UserProjectAssignment, Long> {

    Optional<UserProjectAssignment> findByUser_UserId(Long userId);

    Optional<UserProjectAssignment> findByUser(User user);
//    Optional<UserProjectAssignment> findByUserIdAndProjectId(Long userId, Long projectId);
}
