package com.max_kliuba.maxgeoquiz;

import java.io.Serializable;

public class Answer implements Serializable {
    private static final int NO_ANSWER = 0;
    private static final int INCORRECT_ANSWER = 1;
    private static final int CORRECT_ANSWER = 2;
    private static final int CHEAT_ANSWER = 3;

    private int userAnswer;


    public Answer() {
        reset();
    }

    public void reset() {
        setUserAnswer(NO_ANSWER);
    }

    public void setUserAnswer(int currentAnswer) {
        userAnswer = currentAnswer;
    }

    public int getUserAnswer() {
        return userAnswer;
    }

    public boolean hasUserAnswer() {
        return getUserAnswer() != NO_ANSWER;
    }

    public boolean isUserAnswerCorrect() {
        return getUserAnswer() == CORRECT_ANSWER;
    }

    public boolean checkUserAnswer(boolean currentAnswer, boolean correctAnswer) {
        int answer = currentAnswer == correctAnswer ? CORRECT_ANSWER : INCORRECT_ANSWER;
        setUserAnswer(answer);

        return isUserAnswerCorrect();
    }

    public boolean isCheat() {
        return getUserAnswer() == CHEAT_ANSWER;
    }

    public void setCheat(boolean cheat) {
        setUserAnswer(CHEAT_ANSWER);
    }
}
