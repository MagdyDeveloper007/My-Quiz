package developer007.magdy.myquiz.ui.quiz;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import developer007.magdy.myquiz.R;
import developer007.magdy.myquiz.activities.StudentActivity;
import developer007.magdy.myquiz.database.quiz.QuizDatabase;
import developer007.magdy.myquiz.database.quiz.StudentsQuestions;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QuizFragment extends Fragment {

    EditText etQuizQuestion, etQuizOP1, etQuizOP2, etQuizOP3, etQuizOP4;
    String quizQuestion, quizOP1, quizOP2, quizOP3, quizOP4;
    CheckBox cbQuizA, cbQuizB, cbQuizC, cbQuizD;
    AppCompatButton btnQuizSave;
    QuizDatabase quizDatabase;
    int correctOpn;
    private static final String TAG = "QuizFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_quiz, container, false);
        //   AppCompatActivity appCompatActivity = (AppCompatActivity) root.getContext();
        AppCompatActivity appCompatActivity = (AppCompatActivity) root.getContext();
        quizDatabase = QuizDatabase.getInstance(appCompatActivity);

        etQuizQuestion = root.findViewById(R.id.etQuizQuestion);
        etQuizOP1 = root.findViewById(R.id.etQuizOP1);
        etQuizOP2 = root.findViewById(R.id.etQuizOP2);
        etQuizOP3 = root.findViewById(R.id.etQuizOP3);
        etQuizOP4 = root.findViewById(R.id.etQuizOP4);
        cbQuizA = root.findViewById(R.id.cbQuizA);
        cbQuizB = root.findViewById(R.id.cbQuizB);
        cbQuizC = root.findViewById(R.id.cbQuizC);
        cbQuizD = root.findViewById(R.id.cbQuizD);
        btnQuizSave = root.findViewById(R.id.btnQuizSave);
        cbQuizA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbQuizB.setChecked(false);
                cbQuizC.setChecked(false);
                cbQuizD.setChecked(false);
                correctOpn = 1;
            }
        });
        cbQuizB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbQuizA.setChecked(false);
                cbQuizC.setChecked(false);
                cbQuizD.setChecked(false);
                correctOpn = 2;

            }
        });
        cbQuizC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbQuizA.setChecked(false);
                cbQuizB.setChecked(false);
                cbQuizD.setChecked(false);
                correctOpn = 3;

            }
        });
        cbQuizD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbQuizA.setChecked(false);
                cbQuizB.setChecked(false);
                cbQuizC.setChecked(false);
                correctOpn = 4;

            }
        });


        btnQuizSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                InputMethodManager imm = (InputMethodManager) appCompatActivity.getSystemService(appCompatActivity.INPUT_METHOD_SERVICE);
                if (appCompatActivity.getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(appCompatActivity.getCurrentFocus().getWindowToken(), 0);
                }


                quizQuestion = etQuizQuestion.getText().toString().trim();
                quizOP1 = etQuizOP1.getText().toString().trim();
                quizOP2 = etQuizOP2.getText().toString().trim();
                quizOP3 = etQuizOP3.getText().toString().trim();
                quizOP4 = etQuizOP4.getText().toString().trim();

                if (TextUtils.isEmpty(quizQuestion) || TextUtils.isEmpty(quizOP1)
                        || TextUtils.isEmpty(quizOP2) || TextUtils.isEmpty(quizOP3)
                        || TextUtils.isEmpty(quizOP4)) {
                    Snackbar.make(v, "Please fill the empty data", Snackbar.LENGTH_SHORT)
                            .show();
                    return;

                } else if (!cbQuizA.isChecked() && !cbQuizB.isChecked() && !cbQuizC.isChecked() && !cbQuizD.isChecked()) {
                    Snackbar.make(v, "Please check on the correct option", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                StudentsQuestions questions = new StudentsQuestions(quizQuestion, quizOP1, quizOP2, quizOP3, quizOP4, correctOpn);
                quizDatabase.quizDao().insertQuestion(questions).observeOn(Schedulers.io())
                        .subscribeOn(Schedulers.computation())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {

                                Snackbar.make(v, "Saved successfully", Snackbar.LENGTH_SHORT)
                                        .show();


                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                Log.d(TAG, "onError: mgd" + e);
                                Snackbar.make(v, "Error!" + e.getMessage().toString(), Snackbar.LENGTH_SHORT)
                                        .show();


                            }
                        });
                etQuizQuestion.setText("");
                etQuizOP1.setText("");
                etQuizOP2.setText("");
                etQuizOP3.setText("");
                etQuizOP4.setText("");

            }
        });

        return root;
    }
}