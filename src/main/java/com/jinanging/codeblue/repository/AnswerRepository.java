package com.jinanging.codeblue.repository;

import com.jinanging.codeblue.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    // 특정 문제에 달린 선택지들 조회
    List<Answer> findByQuestionQuestionId(Integer questionId);

    // 특정 문제의 정답 선택지 조회
    Optional<Answer> findByQuestionQuestionIdAndIsCorrectTrue(Integer questionId);
}