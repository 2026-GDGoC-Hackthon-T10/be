package com.jinanging.codeblue.service;

import com.jinanging.codeblue.domain.Answer;
import com.jinanging.codeblue.domain.Question;
import com.jinanging.codeblue.domain.UserProgress;
import com.jinanging.codeblue.domain.UserQuestionHistory;
import com.jinanging.codeblue.dto.QuizResultResponse;
import com.jinanging.codeblue.repository.AnswerRepository;
import com.jinanging.codeblue.repository.QuestionRepository;
import com.jinanging.codeblue.repository.UserProgressRepository;
import com.jinanging.codeblue.repository.UserQuestionHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final UserProgressRepository userProgressRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserQuestionHistoryRepository historyRepository;

    @Transactional
    public QuizResultResponse submitAnswer(String sessionId, Integer questionId, String selectedOption) {
        // 1. 엔티티 조회
        UserProgress user = userProgressRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("문제를 찾을 수 없습니다."));

        // 2. 레포지토리 메서드를 활용해 정답 정보 바로 가져오기
        Answer correctAnswer = answerRepository.findByQuestionQuestionIdAndIsCorrectTrue(questionId)
                .orElseThrow(() -> new IllegalStateException("해당 문제에 정답 설정이 없습니다."));

        // 3. 채점
        boolean isCorrect = correctAnswer.getOptionText().equals(selectedOption);

        // 4. 유저 상태 업데이트
        if (isCorrect) {
            user.setScore(user.getScore() + 10);
            user.setCorrectCount(user.getCorrectCount() + 1);
        } else {
            user.setWrongCount(user.getWrongCount() + 1);
        }

        // [중요!] 4-1. 스테이지 내 문제 진행 번호 증가
        // 이 로직이 있어야 6문제를 차례대로 풀고 있다는 것이 기록됩니다.
        user.setCurrentProblemIndex(user.getCurrentProblemIndex() + 1);

        // 5. 풀이 이력(History) 저장
        UserQuestionHistory history = UserQuestionHistory.builder()
                .userProgress(user)
                .question(question)
                .isCorrect(isCorrect)
                .selectedAnswer(selectedOption)
                .attemptCount(1)
                .build();

        historyRepository.save(history);

        // Dirty Checking 덕분에 save를 명시하지 않아도 되지만, 명확성을 위해 추가할 수 있습니다.
        userProgressRepository.save(user);

        // 6. DTO 결과 반환
        return new QuizResultResponse(
                isCorrect,
                user.getScore(),
                question.getExplanation(),
                correctAnswer.getOptionText()
        );
    }
}