package com.jinanging.codeblue.domain;

// javax -> jakarta로 전부 통일!
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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

    @Column(nullable = false)
    private Integer score = 0;

    @Column(name = "correct_count", nullable = false)
    private Integer correctCount = 0;

    @Column(name = "wrong_count", nullable = false)
    private Integer wrongCount = 0;

    // 아까 SQL에 넣었던 시간 설정도 추가해두면 좋습니다.
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}