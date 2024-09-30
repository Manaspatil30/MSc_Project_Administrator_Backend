package com.app.msc_project_administrator.studentChoices;

import com.app.msc_project_administrator.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentChoiceRepository extends JpaRepository<StudentChoice, Long> {
    StudentChoice findByStudent(User student);

    Optional<StudentChoice> findByStudentUserId(Long studentId);

    List<StudentChoice>  findByProjectsProjectId(Integer projectId);

    List<StudentChoice> findByProjectsProjectIdIn(List<Long> projectIds);
}
