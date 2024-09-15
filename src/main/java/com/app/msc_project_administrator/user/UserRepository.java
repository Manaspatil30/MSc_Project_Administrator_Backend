package com.app.msc_project_administrator.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.app.msc_project_administrator.project.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findById(Long id);
//  List<User> findAllById(List<Integer> ids);

  List<User> findAllByUserIdIn(List<Integer> ids);

  List<User> findByRole(Role role);

  List<User> findByExpertiseTagsIn(Set<Tag> tags);

  // Fetch supervisors who have worked on projects with the given tags (expertise)
  @Query("SELECT DISTINCT u FROM User u JOIN u.projects p JOIN p.tags t WHERE t IN :tags AND u.role = 'ACADEMIC'")
  List<User> findSupervisorsByTags(@Param("tags") Set<Tag> tags);

  // Fetch all other supervisors who do not have matching tags
  @Query("SELECT DISTINCT u FROM User u WHERE u.role = 'ACADEMIC' AND NOT EXISTS (SELECT p FROM Project p JOIN p.tags t WHERE t IN :tags AND p.supervisor = u)")
  List<User> findAllSupervisorsExcludingTags(@Param("tags") Set<Tag> tags);

  // Find all students assigned to a specific supervisor
  @Query("SELECT u FROM User u JOIN u.projects p WHERE p.supervisor.userId = :supervisorId AND u.role = 'STUDENT'")
  List<User> findStudentsBySupervisorId(@Param("supervisorId") Long supervisorId);

}
