package com.app.msc_project_administrator.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  Optional<User> findById(Integer id);
//  List<User> findAllById(List<Integer> ids);

  List<User> findAllByUserIdIn(List<Integer> ids);

  List<User> findByRole(Role role);

}
