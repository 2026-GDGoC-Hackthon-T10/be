package com.jinanging.codeblue.service;

import com.jinanging.codeblue.domain.*;
import com.jinanging.codeblue.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GameService {

    private final UserProgressRepository userProgressRepository;
    private final UserQuestionHistoryRepository historyRepository;
    private final QuestionRepository questionRepository;

    private UserProgress getOrCreateUser(String sessionId) {
        return userProgressRepository.findById(sessionId)
                .orElseGet(() -> userProgressRepository.save(UserProgress.builder()
                        .sessionId(sessionId)
                        .currentGroup("NONE")
                        .stage1Clear(false).stage2Clear(false).stage3Clear(false)
                        .stage4Clear(false).stage5Clear(false).stage6Clear(false).stage7Clear(false)
                        .score(0)
                        .correctCount(0)
                        .wrongCount(0)
                        .build()));
    }

    /**
     * 1. 스테이지 진입 체크
     */
    public Map<String, Object> tryEnterStage(Integer stage, HttpSession session) {
        UserProgress user = getOrCreateUser(session.getId());
        Map<String, Object> result = new HashMap<>();

        if (stage == 7) {
            if (user.isAllBasicStagesCleared()) {
                result.put("canEnter", true);
                result.put("message", "최종 시스템 복구 스테이지에 진입합니다.");
            } else {
                result.put("canEnter", false);
                result.put("message", "아직 복구되지 않은 구역이 있습니다. 1~6 스테이지를 모두 완료하세요.");
            }
            return result;
        }

        String targetGroup = user.getGroupOfStage(stage);
        String currentGroup = user.getCurrentGroup();

        if ("NONE".equals(currentGroup) || currentGroup.equals(targetGroup)) {
            if ("NONE".equals(currentGroup)) {
                user.setCurrentGroup(targetGroup);
                userProgressRepository.saveAndFlush(user);
            }
            result.put("canEnter", true);
            result.put("currentGroup", user.getCurrentGroup());
            result.put("message", targetGroup + " 그룹 임무를 수행합니다.");
        } else {
            result.put("canEnter", false);
            result.put("message", "현재 진행 중인 " + currentGroup + " 구역을 먼저 완료해야 합니다!");
        }

        return result;
    }

    /**
     * 2. 스테이지별 문제 추출 (6개)
     */
    public List<Map<String, Object>> getQuestionsByStage(Integer stage, HttpSession session) {
        UserProgress user = getOrCreateUser(session.getId());
        user.setCurrentProblemIndex(0);
        userProgressRepository.save(user);

        List<Question> stageQuestions = questionRepository.findByStage(stage);
        if (stageQuestions.isEmpty()) return new ArrayList<>();

        Collections.shuffle(stageQuestions);
        return stageQuestions.stream()
                .limit(6)
                .map(q -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("questionId", q.getQuestionId());
                    map.put("questionText", q.getQuestionText());
                    map.put("questionType", q.getQuestionType());
                    map.put("contextText", q.getContextText());
                    map.put("options", q.getAnswers().stream()
                            .map(Answer::getOptionText)
                            .collect(Collectors.toList()));
                    return map;
                }).collect(Collectors.toList());
    }

    /**
     * 3. 스테이지 클리어 처리 및 그룹 해제
     */
    public Map<String, Object> clearStage(Integer stage, HttpSession session) {
        UserProgress user = getOrCreateUser(session.getId());

        if (stage == 1) user.setStage1Clear(true);
        else if (stage == 2) user.setStage2Clear(true);
        else if (stage == 3) user.setStage3Clear(true);
        else if (stage == 4) user.setStage4Clear(true);
        else if (stage == 5) user.setStage5Clear(true);
        else if (stage == 6) user.setStage6Clear(true);
        else if (stage == 7) user.setStage7Clear(true);

        boolean groupFinished = false;
        String myGroup = user.getCurrentGroup();

        if ("A".equals(myGroup) && user.isStage1Clear() && user.isStage3Clear()) groupFinished = true;
        else if ("B".equals(myGroup) && user.isStage2Clear() && user.isStage5Clear()) groupFinished = true;
        else if ("C".equals(myGroup) && user.isStage4Clear() && user.isStage6Clear()) groupFinished = true;

        if (groupFinished) {
            user.setCurrentGroup("NONE");
        }

        userProgressRepository.saveAndFlush(user);

        Map<String, Object> res = new HashMap<>();
        res.put("result", "success");
        res.put("isGroupFinished", groupFinished);
        res.put("currentStatus", user.getCurrentGroup());
        res.put("isFinalOpen", user.isAllBasicStagesCleared());
        return res;
    }

    /**
     * 4. 최종 리포트 데이터 조회
     */
    public Map<String, Object> getFinalReport(HttpSession session) {
        UserProgress user = getOrCreateUser(session.getId());
        List<UserQuestionHistory> allHistories = historyRepository.findByUserProgressSessionId(session.getId());

        List<Map<String, Object>> wrongList = allHistories.stream()
                .filter(h -> h.getIsCorrect() != null && !h.getIsCorrect())
                .map(h -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("questionId", h.getQuestion().getQuestionId());
                    m.put("questionText", h.getQuestion().getQuestionText());
                    m.put("myAnswer", h.getSelectedAnswer());
                    return m;
                }).collect(Collectors.toList());

        Map<String, Object> report = new HashMap<>();
        report.put("totalCorrect", user.getCorrectCount());
        report.put("totalWrong", user.getWrongCount());
        report.put("totalScore", user.getScore());
        report.put("wrongQuestions", wrongList);
        return report;
    }

    /**
     * 5. 광고 시청 후 해설 잠금 해제
     */
    public List<Map<String, Object>> unlockExplanations(HttpSession session) {
        List<UserQuestionHistory> allHistories = historyRepository.findByUserProgressSessionId(session.getId());
        return allHistories.stream()
                .filter(h -> h.getIsCorrect() != null && !h.getIsCorrect())
                .map(h -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("questionId", h.getQuestion().getQuestionId());
                    m.put("explanation", h.getQuestion().getExplanation());
                    return m;
                }).collect(Collectors.toList());
    }
}