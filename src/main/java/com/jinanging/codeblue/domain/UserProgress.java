package com.jinanging.codeblue.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_progress")
public class UserProgress {

    @Id
    @Column(name = "session_id", length = 255)
    private String sessionId;

    // 현재 진행 중인 그룹: "NONE", "A", "B", "C" (A: 1,3 / B: 2,5 / C: 4,6)
    @Builder.Default
    @Column(name = "current_group")
    private String currentGroup = "NONE";

    // 현재 스테이지 내에서 몇 번째 문제를 풀 차례인지 (0~5)
    @Builder.Default
    @Column(name = "current_problem_index")
    private Integer currentProblemIndex = 0;

    // 각 스테이지별 클리어 여부 (해커톤에선 개별 컬럼이 가장 빠르고 정확함)
    @Builder.Default private boolean stage1Clear = false;
    @Builder.Default private boolean stage2Clear = false;
    @Builder.Default private boolean stage3Clear = false;
    @Builder.Default private boolean stage4Clear = false;
    @Builder.Default private boolean stage5Clear = false;
    @Builder.Default private boolean stage6Clear = false;
    @Builder.Default private boolean stage7Clear = false;

    @Builder.Default
    @Column(nullable = false)
    private Integer score = 0;

    @Builder.Default
    @Column(name = "correct_count", nullable = false)
    private Integer correctCount = 0;

    @Builder.Default
    @Column(name = "wrong_count", nullable = false)
    private Integer wrongCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 스테이지가 속한 그룹을 판별하는 헬퍼 메서드
    public String getGroupOfStage(int stage) {
        if (stage == 1 || stage == 3) return "A";
        if (stage == 2 || stage == 5) return "B";
        if (stage == 4 || stage == 6) return "C";
        return "FINAL";
    }

    // 모든 기본 스테이지(1~6)를 클리어했는지 확인
    public boolean isAllBasicStagesCleared() {
        return stage1Clear && stage2Clear && stage3Clear && stage4Clear && stage5Clear && stage6Clear;
    }
}