package com.max_kliuba.maxgeoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.max_kliuba.maxgeoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.max_kliuba.maxgeoquiz.answer_shown";

    private TextView mCheatQuestionTextView;
    private Button mShowAnswerButton;
    private TextView mAnswerTextView;
    private TextView mApiLevelTextView;

    private boolean mAnswerIsTrue;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mCheatQuestionTextView = (TextView) findViewById(R.id.cheat_question_textview);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Animator cheatQuestionTextViewAnim = ViewAnimationUtils
                            .createCircularReveal(mCheatQuestionTextView,
                                    mCheatQuestionTextView.getWidth() / 2,
                                    mCheatQuestionTextView.getHeight() / 2,
                                    mCheatQuestionTextView.getWidth(), 0);
                    cheatQuestionTextViewAnim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mCheatQuestionTextView.setVisibility(View.INVISIBLE);
                        }
                    });
                    cheatQuestionTextViewAnim.start();

                    Animator showAnswerButtonAnim = ViewAnimationUtils
                            .createCircularReveal(mShowAnswerButton,
                                    mShowAnswerButton.getWidth() / 2,
                                    mShowAnswerButton.getHeight() / 2,
                                    mShowAnswerButton.getWidth(), 0);
                    showAnswerButtonAnim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    showAnswerButtonAnim.start();

                    Animator answerTextViewAnim = ViewAnimationUtils
                            .createCircularReveal(mAnswerTextView,
                                    mAnswerTextView.getWidth() / 2,
                                    mAnswerTextView.getHeight() / 2,
                                    mAnswerTextView.getWidth(), 0);
                    showAnswerButtonAnim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAnswerTextView.setVisibility(View.VISIBLE);
                        }
                    });
                    answerTextViewAnim.start();
                } else {
                    mCheatQuestionTextView.setVisibility(View.INVISIBLE);
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                    mAnswerTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        mApiLevelTextView = (TextView) findViewById(R.id.api_level_textview);
        mApiLevelTextView.setText(getString(R.string.api_level, Build.VERSION.SDK_INT));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
}
