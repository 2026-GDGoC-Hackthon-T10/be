package com.jinanging.codeblue.repository;

import com.jinanging.codeblue.domain.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {
    // 특정 스테이지의 시나리오 목록 조회
    List<Scenario> findByStage(Integer stage);
}