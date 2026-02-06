package com.jinanging.codeblue.controller;

import com.jinanging.codeblue.dto.QuestionResponse;
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
     * [추가] 랜덤 문제 조회
     * 특정 스테이지의 문제 중 하나를 랜덤으로 가져옵니다.
     */
    @GetMapping("/random")
    @Operation(summary = "랜덤 문제 조회", description = "스테이지 번호를 보내 해당 스테이지의 랜덤 문제를 가져옵니다.")
    public ResponseEntity<QuestionResponse> getRandomQuestion(@RequestParam Integer stage) {
        QuestionResponse response = quizService.getRandomQuestion(stage);
        return ResponseEntity.ok(response);
    }

    /**
     * 문제 상세 조회
     * ID를 통해 특정 문제의 지문과 정보를 가져옵니다.
     */
    @GetMapping("/{questionId}")
    @Operation(summary = "문제 상세 조회", description = "ID를 통해 문제 지문과 정보를 가져옵니다.")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Integer questionId) {
        QuestionResponse response = quizService.getQuestionDetail(questionId);
        return ResponseEntity.ok(response);
    }

    /**
     * 문제 정답 제출 및 채점
     * 사용자가 선택한 답을 채점하고, 누적 점수와 해설을 반환합니다.
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