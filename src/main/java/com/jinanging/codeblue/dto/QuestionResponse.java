package com.jinanging.codeblue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

// QuestionResponse.java
@Getter
@AllArgsConstructor
public class QuestionResponse {
    private Integer questionId;
    private Integer stage;
    private String title;
    private String contextText;
    private String type;
    private List<String> options; // <-- 이 필드가 '반드시' 있어야 Swagger에 보기가 나옵니다!
}