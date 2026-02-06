package com.jinanging.codeblue.repository;

import com.jinanging.codeblue.domain.ScenarioFlow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioFlowRepository extends JpaRepository<ScenarioFlow, Integer> {
    // 현재 시나리오에서 연결되는 다음 흐름들 조회
    List<ScenarioFlow> findByCurrentScenarioScenarioId(Integer currentScenarioId);
}