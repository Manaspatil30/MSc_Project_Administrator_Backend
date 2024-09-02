package com.app.msc_project_administrator.studentChoices;

import com.app.msc_project_administrator.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentChoiceRepository extends JpaRepository<StudentChoice, Long> {
    StudentChoice findByStudent(User student);

//    @Query("SELECT sc FROM StudentChoice sc WHERE sc.student.userId = :studentId")
    Optional<StudentChoice> findByStudentUserId(Long studentId);
}
