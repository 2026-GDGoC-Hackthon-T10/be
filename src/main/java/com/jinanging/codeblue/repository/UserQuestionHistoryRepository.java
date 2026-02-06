package com.jinanging.codeblue.repository;

import com.jinanging.codeblue.domain.UserQuestionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// UserQuestionHistoryRepository.java
public interface UserQuestionHistoryRepository extends JpaRepository<UserQuestionHistory, Integer> {
    // 특정 유저의 모든 풀이 기록 조회
    List<UserQuestionHistory> findByUserProgressSessionId(String sessionId);
}