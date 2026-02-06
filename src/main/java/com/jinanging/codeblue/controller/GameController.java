package com.jinanging.codeblue.controller;

import com.jinanging.codeblue.service.GameService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    /**
     * 0. 스테이지 진입 가능 여부 체크
     * 유저가 스테이지 아이콘을 클릭했을 때 가장 먼저 호출합니다.
     * 그룹 고정 여부와 7스테이지 해금 상태를 체크합니다.
     */
    @PostMapping("/stage/{stageNumber}/enter")
    public ResponseEntity<?> tryEnter(@PathVariable Integer stageNumber, HttpSession session) {
        Map<String, Object> result = gameService.tryEnterStage(stageNumber, session);

        // 진입 불가능할 경우(다른 그룹 진행 중 등) 403 반환
        if (!(boolean) result.get("canEnter")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 1. 스테이지 문제 수령
     * 진입 체크(enter) 통과 후, 해당 스테이지의 랜덤 문제 6개를 가져옵니다.
     */
    @GetMapping("/stage/{stageNumber}")
    public ResponseEntity<List<Map<String, Object>>> startStage(
            @PathVariable Integer stageNumber,
            HttpSession session) {
        List<Map<String, Object>> questions = gameService.getQuestionsByStage(stageNumber, session);
        return ResponseEntity.ok(questions);
    }

    /**
     * 2. 스테이지 클리어 처리
     * 6문제를 모두 푼 시점에 호출합니다.
     * 해당 스테이지를 '완료' 처리하고, 그룹이 모두 깨졌는지 확인합니다.
     */
    @PostMapping("/stage/{stageNumber}/clear")
    public ResponseEntity<?> clearStage(@PathVariable Integer stageNumber, HttpSession session) {
        return ResponseEntity.ok(gameService.clearStage(stageNumber, session));
    }

    /**
     * 3. 최종 결과 리포트
     * 게임 종료 후 전체 성적과 틀린 문제 리스트를 가져옵니다.
     */
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getFinalReport(HttpSession session) {
        return ResponseEntity.ok(gameService.getFinalReport(session));
    }

    /**
     * 4. 광고 후 해설 해제
     * 광고 시청 완료 후 호출하면 오답들에 대한 해설(explanation)을 반환합니다.
     */
    @PostMapping("/report/unlock")
    public ResponseEntity<List<Map<String, Object>>> unlockExplanations(HttpSession session) {
        return ResponseEntity.ok(gameService.unlockExplanations(session));
    }
}