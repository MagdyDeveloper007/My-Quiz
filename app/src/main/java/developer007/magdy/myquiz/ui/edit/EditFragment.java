package developer007.magdy.myquiz.ui.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import developer007.magdy.myquiz.R;
import developer007.magdy.myquiz.database.editQuiz.EditQuizAdapter;
import developer007.magdy.myquiz.database.quiz.QuizDatabase;
import developer007.magdy.myquiz.database.quiz.StudentsQuestions;
import developer007.magdy.myquiz.database.student.StudentAnswerDatabase;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditFragment extends Fragment {
    private EditQuizAdapter editQuizAdapter;
    private RecyclerView recyclerReportEdit;
    private TextView tvNoQuizAdded;
    private QuizDatabase quizDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_edit_quiz, container, false);
        tvNoQuizAdded = root.findViewById(R.id.tvNoQuizAdded);
        recyclerReportEdit = root.findViewById(R.id.recyclerReportEdit);
        recyclerReportEdit.setLayoutManager(new LinearLayoutManager(getContext()));

        tvNoQuizAdded.setVisibility(View.GONE);
        recyclerReportEdit.setVisibility(View.VISIBLE);

        editQuizAdapter = new EditQuizAdapter();
        recyclerReportEdit.setAdapter(editQuizAdapter);

        quizDatabase = QuizDatabase.getInstance(getContext());


        quizDatabase.quizDao().getQuestions().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<StudentsQuestions>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<StudentsQuestions> studentsQuestions) {
                        if (studentsQuestions.size() > 0) {
                            editQuizAdapter.setList(studentsQuestions);
                            editQuizAdapter.notifyDataSetChanged();
                        } else {
                            tvNoQuizAdded.setVisibility(View.VISIBLE);
                            recyclerReportEdit.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });


        tvNoQuizAdded.setVisibility(View.GONE);
        recyclerReportEdit.setVisibility(View.VISIBLE);


        return root;
    }
}