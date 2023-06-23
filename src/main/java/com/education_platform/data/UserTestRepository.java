package com.education_platform.data;

import com.education_platform.model.UserTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserTestRepository extends CrudRepository<UserTest, Long> {

    Optional<UserTest> findByUserEmailAndTestId(String user_email, Long test_id);

    List<UserTest> findAllByUserEmailAndTest_Module_CourseId(String user_email, Long course_id);

}
