package com.jinanging.codeblue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizResultResponse {
    private boolean isCorrect;
    private Integer currentScore;
    private String explanation;
    private String correctOption;
}
