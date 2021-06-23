package developer007.magdy.myquiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import developer007.magdy.myquiz.R;
import developer007.magdy.myquiz.database.quiz.QuizDatabase;
import developer007.magdy.myquiz.database.SharedPrefManager;
import developer007.magdy.myquiz.database.quiz.StudentsQuestions;
import developer007.magdy.myquiz.database.student.StudentAnswer;
import developer007.magdy.myquiz.database.student.StudentAnswerDatabase;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class StudentActivity extends AppCompatActivity {
    private FloatingActionButton studentFab;
    private AppCompatButton btnStudentNext, btnStudentBack, btnStudentFinish;
    private int soundClick, pageCount;
    private MediaPlayer mediaPlayer;
    private String userName;
    String strSilent = "off";
    String saveScoreKey;

    private String strQuestion, strOpn1, strOpn2, strOpn3, strOpn4;
    private TextView tvQuestion, tvQuizEmpty, tvFinalScore;
    private RadioButton rb1, rb2, rb3, rb4;
    private LinearLayout linear1, linearQuizFinished;
    private SharedPrefManager sharedPrefManager;
    private String key, strUserTheme, strSavedTheme;
    private QuizDatabase quizDatabase;
    private int studentOpn, correctOpn;
    private StudentAnswerDatabase studentAnswerDatabase;

    private static final String TAG = "StudentActivity";
    private String finished;
    float score, total;
    //List<String> scoreList = new ArrayList<>();
    Map<String, String> scoreList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        score = 0;
        pageCount = 0;
        total = 0;
        linear1 = findViewById(R.id.linear1);
        studentFab = findViewById(R.id.studentFab);
        tvQuestion = findViewById(R.id.tvQuestion);
        linearQuizFinished = findViewById(R.id.linearQuizFinished);
        tvQuizEmpty = findViewById(R.id.tvQuizEmpty);
        tvFinalScore = findViewById(R.id.tvFinalScore);
        btnStudentNext = findViewById(R.id.btnStudentNext);
        btnStudentBack = findViewById(R.id.btnStudentBack);
        btnStudentFinish = findViewById(R.id.btnStudentFinish);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        linearQuizFinished.setVisibility(View.GONE);
        tvQuizEmpty.setVisibility(View.VISIBLE);
        linear1.setVisibility(View.GONE);

        key = "id";
        sharedPrefManager = new SharedPrefManager();


        userName = sharedPrefManager.getAuthPref(this).getString("userName", "");
        strUserTheme = userName + "theme";

        strSavedTheme = sharedPrefManager.getAuthPref(this).getString(strUserTheme, "").trim();
        if (strSavedTheme.equals("theme_one")) {
            linear1.setBackgroundColor(Color.WHITE);
            themeSelection(1);

        } else if (strSavedTheme.equals("theme_two")) {
            themeSelection(2);

        } else if (strSavedTheme.equals("theme_three")) {
            themeSelection(3);

        }

        String keyFinish = userName + "finished";
        finished = sharedPrefManager.getAuthPref(this).getString(keyFinish, "");
        if (!TextUtils.isEmpty(finished)) {
            linearQuizFinished.setVisibility(View.VISIBLE);
            checkScore(userName);
            tvQuizEmpty.setVisibility(View.GONE);
            linear1.setVisibility(View.GONE);
            return;
        }
        quizDatabase = QuizDatabase.getInstance(this);

        quizDatabase.quizDao().getQuestions().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<StudentsQuestions>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<StudentsQuestions> studentsQuestions) {

                        if (studentsQuestions.size() > 0) {
                            total = studentsQuestions.size();
                            saveScoreKey = userName + total;
                            linearQuizFinished.setVisibility(View.GONE);
                            tvQuizEmpty.setVisibility(View.GONE);
                            linear1.setVisibility(View.VISIBLE);
                            strQuestion = studentsQuestions.get(pageCount).getQuiz();
                            strOpn1 = studentsQuestions.get(pageCount).getOpt1();
                            strOpn2 = studentsQuestions.get(pageCount).getOpt2();
                            strOpn3 = studentsQuestions.get(pageCount).getOpt3();
                            strOpn4 = studentsQuestions.get(pageCount).getOpt4();
                            tvQuestion.setText(strQuestion);
                            rb1.setText(strOpn1);
                            rb2.setText(strOpn2);
                            rb3.setText(strOpn3);
                            rb4.setText(strOpn4);
                        } else {
                            linear1.setVisibility(View.GONE);
                            linearQuizFinished.setVisibility(View.GONE);
                            tvQuizEmpty.setVisibility(View.VISIBLE);
                        }
                        if (studentsQuestions.size() == 1) {
                            btnStudentNext.setVisibility(View.GONE);
                            btnStudentFinish.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: mgd" + e);
                    }
                });


        soundClick = 1;
        mediaPlayer = MediaPlayer.create(StudentActivity.this, R.raw.click);
        if (soundClick == 1) {
            mediaPlayer.start();
        }
        btnStudentNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                studentOpn = 0;
                if (rb1.isChecked()) {
                    studentOpn = 1;
                } else if (rb2.isChecked()) {
                    studentOpn = 2;
                } else if (rb3.isChecked()) {
                    studentOpn = 3;
                } else if (rb4.isChecked()) {
                    studentOpn = 4;
                }
                if (studentOpn == 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    Snackbar.make(v, "Please select an answer", Snackbar.LENGTH_LONG).show();


                    return;
                }
                pageCount += 1;
                if (pageCount > 0) {
                    btnStudentBack.setVisibility(View.VISIBLE);
                }
                if (soundClick == 1) {
                    mediaPlayer.start();
                }
                quizDatabase.quizDao().getQuestions().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<StudentsQuestions>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull List<StudentsQuestions> studentsQuestions) {
                                if (pageCount == studentsQuestions.size() - 1) {
                                    btnStudentNext.setVisibility(View.GONE);
                                    btnStudentFinish.setVisibility(View.VISIBLE);
                                }
                                if (studentsQuestions.size() > 0) {
                                    strQuestion = studentsQuestions.get(pageCount).getQuiz();
                                    strOpn1 = studentsQuestions.get(pageCount).getOpt1();
                                    strOpn2 = studentsQuestions.get(pageCount).getOpt2();
                                    strOpn3 = studentsQuestions.get(pageCount).getOpt3();
                                    strOpn4 = studentsQuestions.get(pageCount).getOpt4();
                                    correctOpn = studentsQuestions.get(pageCount).getCorrectOpn();
                                    tvQuestion.setText(strQuestion);

                                    rb1.setText(strOpn1);
                                    rb2.setText(strOpn2);
                                    rb3.setText(strOpn3);
                                    rb4.setText(strOpn4);
                                } else {
                                    linear1.setVisibility(View.GONE);
                                }


                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d(TAG, "onError: mgd" + e);
                            }
                        });
                if (studentOpn == correctOpn) {
                    score += 1;
                    scoreList.put("" + pageCount, "" + score);


                } else if (scoreList.get("" + pageCount) != null) {

                    scoreList.remove("" + pageCount);
                }

            }
        });
        btnStudentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pageCount -= 1;
                btnStudentNext.setVisibility(View.VISIBLE);
                btnStudentFinish.setVisibility(View.GONE);
                if (pageCount <= 0) {
                    btnStudentBack.setVisibility(View.GONE);
                }
                if (soundClick == 1) {
                    mediaPlayer.start();
                }
                quizDatabase.quizDao().getQuestions().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<StudentsQuestions>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull List<StudentsQuestions> studentsQuestions) {
                                if (studentsQuestions.size() > 0) {
                                    strQuestion = studentsQuestions.get(pageCount).getQuiz();
                                    strOpn1 = studentsQuestions.get(pageCount).getOpt1();
                                    strOpn2 = studentsQuestions.get(pageCount).getOpt2();
                                    strOpn3 = studentsQuestions.get(pageCount).getOpt3();
                                    strOpn4 = studentsQuestions.get(pageCount).getOpt4();
                                    tvQuestion.setText(strQuestion);
                                    rb1.setText(strOpn1);
                                    rb2.setText(strOpn2);
                                    rb3.setText(strOpn3);
                                    rb4.setText(strOpn4);
                                } else {
                                    linear1.setVisibility(View.GONE);
                                }


                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d(TAG, "onError: mgd" + e);
                            }
                        });
            }
        });
        btnStudentFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                studentOpn = 0;
                if (rb1.isChecked()) {
                    studentOpn = 1;
                } else if (rb2.isChecked()) {
                    studentOpn = 2;
                } else if (rb3.isChecked()) {
                    studentOpn = 3;
                } else if (rb4.isChecked()) {
                    studentOpn = 4;
                }
                if (studentOpn == 0) {
                    Snackbar.make(v, "Please select an answer", Snackbar.LENGTH_LONG).show();
                    return;
                }
                pageCount += 1;

                String keyFinish = userName + "finished";

                quizDatabase.quizDao().getQuestions().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<StudentsQuestions>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull List<StudentsQuestions> studentsQuestions) {

                                if (studentsQuestions.size() > 0) {

                                    strQuestion = studentsQuestions.get(pageCount-1).getQuiz();
                                    strOpn1 = studentsQuestions.get(pageCount-1).getOpt1();
                                    strOpn2 = studentsQuestions.get(pageCount-1).getOpt2();
                                    strOpn3 = studentsQuestions.get(pageCount-1).getOpt3();
                                    strOpn4 = studentsQuestions.get(pageCount-1).getOpt4();
                                    correctOpn = studentsQuestions.get(pageCount-1).getCorrectOpn();
                                    tvQuestion.setText(strQuestion);

                                    rb1.setText(strOpn1);
                                    rb2.setText(strOpn2);
                                    rb3.setText(strOpn3);
                                    rb4.setText(strOpn4);
                                    if (studentOpn == correctOpn) {
                                        score += 1;
                                        scoreList.put("" + pageCount, "" + score);

                                    } else if (scoreList.get("" + pageCount) != null) {

                                        scoreList.remove("" + pageCount);
                                    }
                                    scoreList.size();
                                    calcPercent(scoreList.size(), total);
                                } else {
                                    linear1.setVisibility(View.GONE);
                                }


                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d(TAG, "onError: mgd" + e);
                            }
                        });

                sharedPrefManager.setPrefVal(StudentActivity.this,
                        keyFinish, "finished");
                //calcPercent(score, total);
                linearQuizFinished.setVisibility(View.VISIBLE);
                tvQuizEmpty.setVisibility(View.GONE);
                linear1.setVisibility(View.GONE);

            }
        });
        studentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Turn sounds " + strSilent, Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (soundClick == 1) {
                                    studentFab.setImageResource(R.drawable.ic_baseline_hearing_disabled);
                                    soundClick = 0;
                                    strSilent = "on";
                                } else {
                                    studentFab.setImageResource(R.drawable.ic_baseline_hearing);
                                    soundClick = 1;
                                    strSilent = "off";

                                }
                            }
                        }).show();
            }
        });


    }

    public void calcPercent(float score, float total) {
        float percent = (score / total) * 100;
        saveScore(percent);

    }

    public void saveScore(float percent) {

        tvFinalScore.setText(" " + percent);
        sharedPrefManager.setPrefVal(StudentActivity.this, saveScoreKey, "" + percent);
        sharedPrefManager.setPrefVal(StudentActivity.this, userName, "" + percent);

        StudentAnswer studentAnswer = new StudentAnswer(userName.substring(0, 8) + pageCount, "" + percent);
        studentAnswerDatabase = StudentAnswerDatabase.getInstance(StudentActivity.this);

        studentAnswerDatabase.studentDao().insertAnswer(studentAnswer)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: mgd");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: StudentActivity mgd" + e);
                        Toast.makeText(StudentActivity.this, "Error!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


    }

    public void checkScore(String userName) {
        String key = userName;

        String score = sharedPrefManager.getAuthPref(StudentActivity.this).getString(key, "");
        tvFinalScore.setText(score);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {

        strUserTheme = userName + "theme";
        switch (item.getItemId()) {
            case R.id.theme_one:
                themeSelection(1);

                sharedPrefManager.setPrefVal(this, strUserTheme, "theme_one");
                break;
            case R.id.theme_two:
                themeSelection(2);
                sharedPrefManager.setPrefVal(this, strUserTheme, "theme_two");

                break;
            case R.id.theme_three:
                themeSelection(3);

                sharedPrefManager.setPrefVal(this, strUserTheme, "theme_three");

                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public void themeSelection(int id) {
        if (id == 1) {
            linear1.setBackgroundColor(Color.WHITE);
            rb1.setTextColor(getResources().getColor(R.color.black));
            rb2.setTextColor(getResources().getColor(R.color.black));
            rb3.setTextColor(getResources().getColor(R.color.black));
            rb4.setTextColor(getResources().getColor(R.color.black));
            tvQuestion.setTextColor(getResources().getColor(R.color.black));
            btnStudentNext.setBackgroundResource(R.drawable.rounded_text);
            btnStudentBack.setBackgroundResource(R.drawable.rounded_text);
            btnStudentFinish.setBackgroundResource(R.drawable.rounded_text);
            btnStudentNext.setTextColor(getResources().getColor(R.color.purple_500));
            btnStudentBack.setTextColor(getResources().getColor(R.color.purple_500));
            btnStudentFinish.setTextColor(getResources().getColor(R.color.purple_500));
        } else if (id == 2) {
            linear1.setBackgroundColor(0xFFBCA463);
            rb1.setTextColor(getResources().getColor(R.color.white));
            rb2.setTextColor(getResources().getColor(R.color.white));
            rb3.setTextColor(getResources().getColor(R.color.white));
            rb4.setTextColor(getResources().getColor(R.color.white));
            tvQuestion.setTextColor(getResources().getColor(R.color.white));
            btnStudentNext.setBackgroundResource(R.drawable.rounded_button);
            btnStudentBack.setBackgroundResource(R.drawable.rounded_button);
            btnStudentFinish.setBackgroundResource(R.drawable.rounded_button);
            btnStudentNext.setTextColor(Color.WHITE);
            btnStudentBack.setTextColor(Color.WHITE);
            btnStudentFinish.setTextColor(Color.WHITE);
        } else if (id == 3) {
            linear1.setBackgroundColor(Color.WHITE);
            rb1.setTextColor(getResources().getColor(R.color.purple_200));
            rb2.setTextColor(getResources().getColor(R.color.purple_200));
            rb3.setTextColor(getResources().getColor(R.color.purple_200));
            rb4.setTextColor(getResources().getColor(R.color.purple_200));
            tvQuestion.setTextColor(getResources().getColor(R.color.purple_200));
            btnStudentNext.setBackgroundResource(R.drawable.rounded_button);
            btnStudentBack.setBackgroundResource(R.drawable.rounded_button);
            btnStudentFinish.setBackgroundResource(R.drawable.rounded_button);
            btnStudentNext.setTextColor(Color.WHITE);
            btnStudentBack.setTextColor(Color.WHITE);
            btnStudentFinish.setTextColor(Color.WHITE);
        }
    }

}