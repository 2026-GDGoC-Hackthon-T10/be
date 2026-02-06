package com.jinanging.codeblue.repository;

import com.jinanging.codeblue.domain.UserProgress;
import com.jinanging.codeblue.domain.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, String> {
    // 세션 ID로 사용자 점수 정보 조회 (기본 제공)
}
