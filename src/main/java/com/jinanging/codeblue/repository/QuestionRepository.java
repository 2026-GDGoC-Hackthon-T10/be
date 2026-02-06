package com.jinanging.codeblue.repository;

import com.jinanging.codeblue.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    // 특정 스테이지의 모든 문제 조회
    List<Question> findByStage(Integer stage);

    // 특정 난이도 이상의 문제 조회
    List<Question> findByStageAndDifficultyGreaterThanEqual(Integer stage, Integer difficulty);
}