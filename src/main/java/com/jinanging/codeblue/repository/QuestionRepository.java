package com.jinanging.codeblue.repository;

import com.jinanging.codeblue.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findByStage(Integer stage);

    // [추가] 특정 스테이지의 문제 중 랜덤으로 1개만 뽑아옴
    @Query(value = "SELECT * FROM question WHERE stage = :stage ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Question> findRandomByStage(@Param("stage") Integer stage);
}