package com.jinanging.codeblue.repository;

import com.jinanging.codeblue.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    // 특정 스테이지의 문제들을 가져옴 (기획상 스테이지당 1개 혹은 랜덤 추출용)
    List<Question> findByStage(Integer stage);
}