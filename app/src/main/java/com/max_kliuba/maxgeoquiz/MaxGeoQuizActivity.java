package com.max_kliuba.maxgeoquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MaxGeoQuizActivity extends AppCompatActivity {
    private static final String TAG = "MaxGeoQuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWER = "answers";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private Button mCheatButton;
    private Button mResetButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private Answer[] mAnswers = new Answer[mQuestionsBank.length];
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnswers = (Answer[]) savedInstanceState.getSerializable(KEY_ANSWER);
        } else {
            for (int i = 0; i < mAnswers.length; i++) {
                mAnswers[i] = new Answer();
            }
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNextButton.callOnClick();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MaxGeoQuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = mCurrentIndex > 0 ? --mCurrentIndex : mQuestionsBank.length - 1;
                updateQuestion();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = mCurrentIndex < mQuestionsBank.length - 1 ? ++mCurrentIndex : 0;
                updateQuestion();
            }
        });

        mResetButton = (Button) findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = 0;

                for (Answer answer : mAnswers) {
                    answer.reset();
                }
                updateQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mAnswers[mCurrentIndex].setCheat(CheatActivity.wasAnswerShown(data));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putSerializable(KEY_ANSWER, mAnswers);
    }

    private void updateQuestion() {
        mQuestionTextView.setText(mQuestionsBank[mCurrentIndex].getTextResId());
        setAnswerButtonState(!mAnswers[mCurrentIndex].hasUserAnswer());
    }

    private void setAnswerButtonState(boolean state) {
        mTrueButton.setEnabled(state);
        mFalseButton.setEnabled(state);
        mCheatButton.setEnabled(state);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
        int messageResId;

        if (mAnswers[mCurrentIndex].isCheat()) {
            messageResId = R.string.judgment_toast;
        } else {
            messageResId = mAnswers[mCurrentIndex].checkUserAnswer(userPressedTrue, answerIsTrue) ? R.string.correct_toast : R.string.incorrect_toast;
        }

        Toast toast = Toast.makeText(MaxGeoQuizActivity.this, messageResId, Toast.LENGTH_SHORT);
        toast.show();

        setAnswerButtonState(false);

        checkResult();
    }

    private void checkResult() {
        int correctAnswerCounter = 0;

        for (Answer answer : mAnswers) {
            if (!answer.hasUserAnswer()) {
                return;
            }
            if (answer.isUserAnswerCorrect()) {
                correctAnswerCounter++;
            }
        }

        double persent = round(correctAnswerCounter * 100.0 / mAnswers.length, 2);
        Toast toast = Toast.makeText(MaxGeoQuizActivity.this, getString(R.string.result) + persent + "%", Toast.LENGTH_LONG);
        toast.show();

    }

    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}