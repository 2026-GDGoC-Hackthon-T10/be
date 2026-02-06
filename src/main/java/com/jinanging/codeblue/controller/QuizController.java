package com.jinanging.codeblue.controller;

import com.jinanging.codeblue.dto.QuizResultResponse;
import com.jinanging.codeblue.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@Tag(name = "Quiz API", description = "퀴즈 풀이 및 채점")
public class QuizController {

    private final QuizService quizService;

    /**
     * 문제 정답 제출 및 채점
     * 사용자가 선택한 답을 채점하고, 누적 점수와 해설(필요시)을 반환합니다.
     * 내부적으로 UserProgress의 문제 인덱스를 1 증가시킵니다.
     */
    @PostMapping("/submit")
    @Operation(summary = "답안 제출", description = "sessionId, questionId, 선택한 답을 보내 채점 결과를 받습니다.")
    public ResponseEntity<QuizResultResponse> submit(
            @RequestParam String sessionId,
            @RequestParam Integer questionId,
            @RequestParam String selectedOption) {

        // QuizService에서 채점 + 인덱스 증가 + 히스토리 저장을 한 번에 처리
        QuizResultResponse response = quizService.submitAnswer(sessionId, questionId, selectedOption);

        return ResponseEntity.ok(response);
    }
}