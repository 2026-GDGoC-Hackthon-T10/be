package com.jinanging.codeblue.controller;

import com.jinanging.codeblue.dto.QuizResultResponse;
import com.jinanging.codeblue.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@Tag(name = "Quiz API", description = "퀴즈 풀이 및 채점")
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/submit")
    @Operation(summary = "답안 제출", description = "sessionId, questionId, 선택한 답을 보내 채점 결과를 받습니다.")
    public QuizResultResponse submit(
            @RequestParam String sessionId,
            @RequestParam Integer questionId,
            @RequestParam String selectedOption) {

        return quizService.submitAnswer(sessionId, questionId, selectedOption);
    }
}