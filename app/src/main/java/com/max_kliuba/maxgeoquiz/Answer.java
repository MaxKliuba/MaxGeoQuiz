package com.max_kliuba.maxgeoquiz;

import java.io.Serializable;

public class Answer implements Serializable {
    private static final int NO_ANSWER = 0;
    private static final int INCORRECT_ANSWER = 1;
    private static final int CORRECT_ANSWER = 2;
    private boolean mIsCheat;
    private static int mCheatCounter;

    private int mUserAnswer;

    public Answer() {
        reset();
    }

    public void reset() {
        setUserAnswer(NO_ANSWER);
        mIsCheat = false;
        mCheatCounter = 3;
    }

    public boolean hasUserAnswer() {
        return getUserAnswer() != NO_ANSWER;
    }

    public int getUserAnswer() {
        return mUserAnswer;
    }

    public boolean isUserAnswerCorrect() {
        return getUserAnswer() == CORRECT_ANSWER;
    }

    public boolean isUserAnswerIncorrect() {
        return getUserAnswer() == INCORRECT_ANSWER;
    }

    public boolean isCheat() {
        return mIsCheat;
    }

    public void setCheat(boolean cheat) {
        mIsCheat = cheat;
        if (cheat) {
            mCheatCounter--;
        }
    }

    public static int getCheatCounter() {
        return mCheatCounter;
    }

    public void setUserAnswer(int currentAnswer) {
        mUserAnswer = currentAnswer;
    }

    public boolean checkUserAnswer(boolean currentAnswer, boolean correctAnswer) {
        int answer = currentAnswer == correctAnswer ? CORRECT_ANSWER : INCORRECT_ANSWER;
        setUserAnswer(answer);

        return isUserAnswerCorrect();
    }
}
