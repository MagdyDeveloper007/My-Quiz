package developer007.magdy.myquiz.ui.report;

import android.os.Bundle;
import android.util.Log;
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
import developer007.magdy.myquiz.database.report.StudentReportAdapter;
import developer007.magdy.myquiz.database.student.StudentAnswer;
import developer007.magdy.myquiz.database.student.StudentAnswerDatabase;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReportFragment extends Fragment {
    private StudentReportAdapter adapter;
    private RecyclerView recyclerReport;
    private StudentAnswerDatabase studentAnswerDatabase;
    private static final String TAG = "ReportFragment";
    private TextView tvNoQuizDone;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        adapter = new StudentReportAdapter();
        recyclerReport = root.findViewById(R.id.recyclerReport);
        tvNoQuizDone = root.findViewById(R.id.tvNoQuizDone);
        recyclerReport.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReport.setAdapter(adapter);
        studentAnswerDatabase = StudentAnswerDatabase.getInstance(getContext());
        tvNoQuizDone.setVisibility(View.GONE);
        recyclerReport.setVisibility(View.VISIBLE);


        studentAnswerDatabase.studentDao().getReport().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<StudentAnswer>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<StudentAnswer> studentAnswers) {
                        if (studentAnswers.size() > 0) {
                            adapter.setList(studentAnswers);
                            adapter.notifyDataSetChanged();
                        } else {
                            tvNoQuizDone.setVisibility(View.VISIBLE);
                            recyclerReport.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d(TAG, "onError: ReportFragment Mgd: " + e);
                    }
                });


        return root;
    }
}