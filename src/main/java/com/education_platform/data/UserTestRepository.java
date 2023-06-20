package com.education_platform.data;

import com.education_platform.model.UserTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserTestRepository extends CrudRepository<UserTest, Long> {

    Optional<UserTest> findByUserIdAndTestId(Long user_id, Long test_id);
    @Query("DELETE t1\n" +
            "FROM user_test t1\n" +
            "JOIN test t2 ON t1.test = t2.id\n" +
            "JOIN module t3 ON t2.module_id=t3.id\n" +
            "WHERE t3.course_id = :course_id AND t1.user_id=:user_id")
    boolean deleteAllByUserIdAndCourseId(@Param("user_id") Long user_id, @Param("course_id") Long course_id);

}
