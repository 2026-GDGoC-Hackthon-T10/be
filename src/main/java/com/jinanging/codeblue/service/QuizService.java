package com.jinanging.codeblue.service;

import com.jinanging.codeblue.domain.Answer;
import com.jinanging.codeblue.domain.Question;
import com.jinanging.codeblue.domain.UserProgress;
import com.jinanging.codeblue.domain.UserQuestionHistory;
import com.jinanging.codeblue.dto.QuestionResponse;
import com.jinanging.codeblue.dto.QuizResultResponse;
import com.jinanging.codeblue.repository.AnswerRepository;
import com.jinanging.codeblue.repository.QuestionRepository;
import com.jinanging.codeblue.repository.UserProgressRepository;
import com.jinanging.codeblue.repository.UserQuestionHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final UserProgressRepository userProgressRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserQuestionHistoryRepository historyRepository;

    /**
     * 스테이지별 랜덤 문제 조회 (보기 포함)
     */
    @Transactional(readOnly = true)
    public QuestionResponse getRandomQuestion(Integer stage) {
        // 1. DB에서 랜덤으로 문제 한 개 가져오기
        Question question = questionRepository.findRandomByStage(stage)
                .orElseThrow(() -> new EntityNotFoundException(stage + " 스테이지에 문제가 없습니다."));

        // 2. 해당 문제에 딸린 모든 보기(Answer) 가져와서 텍스트 리스트로 변환
        List<String> options = answerRepository.findByQuestionQuestionId(question.getQuestionId())
                .stream()
                .map(Answer::getOptionText)
                .collect(Collectors.toList());

        // 3. DTO로 변환하여 반환 (보기 리스트 포함)
        return new QuestionResponse(
                question.getQuestionId(),
                question.getStage(),
                question.getQuestionText(),
                question.getContextText(),
                question.getQuestionType(),
                options
        );
    }

    /**
     * 특정 ID로 문제 상세 조회 (보기 포함)
     */
    @Transactional(readOnly = true)
    public QuestionResponse getQuestionDetail(Integer questionId) {
        // 1. 특정 ID로 문제 조회
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("ID가 " + questionId + "인 문제를 찾을 수 없습니다."));

        // 2. 해당 문제에 딸린 모든 보기 가져오기
        List<String> options = answerRepository.findByQuestionQuestionId(questionId)
                .stream()
                .map(Answer::getOptionText)
                .collect(Collectors.toList());

        // 3. DTO 반환 (보기 리스트 포함)
        return new QuestionResponse(
                question.getQuestionId(),
                question.getStage(),
                question.getQuestionText(),
                question.getContextText(),
                question.getQuestionType(),
                options
        );
    }

    /**
     * 답안 제출 및 채점 (기존 유지)
     */
    @Transactional
    public QuizResultResponse submitAnswer(String sessionId, Integer questionId, String selectedOption) {
        UserProgress user = userProgressRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("문제를 찾을 수 없습니다."));

        Answer correctAnswer = answerRepository.findByQuestionQuestionIdAndIsCorrectTrue(questionId)
                .orElseThrow(() -> new IllegalStateException("해당 문제에 정답 설정이 없습니다."));

        boolean isCorrect = false;
        if (selectedOption != null && correctAnswer.getOptionText() != null) {
            isCorrect = correctAnswer.getOptionText().trim()
                    .equalsIgnoreCase(selectedOption.trim());
        }

        if (isCorrect) {
            user.setScore(user.getScore() + 10);
            user.setCorrectCount(user.getCorrectCount() + 1);
        } else {
            user.setWrongCount(user.getWrongCount() + 1);
        }

        user.setCurrentProblemIndex(user.getCurrentProblemIndex() + 1);

        UserQuestionHistory history = UserQuestionHistory.builder()
                .userProgress(user)
                .question(question)
                .isCorrect(isCorrect)
                .selectedAnswer(selectedOption)
                .attemptCount(1)
                .build();

        historyRepository.save(history);
        userProgressRepository.save(user);

        return new QuizResultResponse(
                isCorrect,
                user.getScore(),
                question.getExplanation(),
                correctAnswer.getOptionText()
        );
    }
}