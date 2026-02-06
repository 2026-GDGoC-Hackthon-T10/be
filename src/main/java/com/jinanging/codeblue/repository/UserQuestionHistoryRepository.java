package com.jinanging.codeblue.repository;

import com.jinanging.codeblue.domain.UserQuestionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserQuestionHistoryRepository extends JpaRepository<UserQuestionHistory, Integer> {
    // 특정 사용자의 전체 풀이 이력 조회
    List<UserQuestionHistory> findByUserProgressSessionId(String sessionId);

    // 특정 사용자가 특정 문제를 푼 적이 있는지 확인
    Optional<UserQuestionHistory> findByUserProgressSessionIdAndQuestionQuestionId(String sessionId, Integer questionId);
}