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

    Optional<UserTest> findByUserIdAndTestId(Long user_id, Long test_id);

    List<UserTest> findAllByUserIdAndTest_Module_CourseId(Long user_id, Long course_id);

}
