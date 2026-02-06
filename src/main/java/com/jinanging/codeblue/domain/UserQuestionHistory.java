package com.jinanging.codeblue.domain;

import com.jinanging.codeblue.domain.Question;
import com.jinanging.codeblue.domain.UserProgress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_question_history")
public class UserQuestionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private UserProgress userProgress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private Boolean isCorrect;
    private String selectedAnswer;
    private Integer attemptCount;

    @CreationTimestamp
    private LocalDateTime createdAt;
}