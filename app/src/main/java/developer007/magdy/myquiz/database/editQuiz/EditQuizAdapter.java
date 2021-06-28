package developer007.magdy.myquiz.database.editQuiz;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import developer007.magdy.myquiz.R;
import developer007.magdy.myquiz.database.quiz.QuizDatabase;
import developer007.magdy.myquiz.database.quiz.StudentsQuestions;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class EditQuizAdapter extends RecyclerView.Adapter<EditQuizAdapter.EditQuizViewHolder> {

    private List<StudentsQuestions> list = new ArrayList<>();
    private int currentId, correctOpn;
    private int newCorrectOpn;
    private String strQuiz, strOpn1, strOpn2, strOpn3, strOpn4;
    private String strNewQuiz, strNewOpn1, strNewOpn2, strNewOpn3, strNewOpn4;
    private QuizDatabase quizDatabase;
    private static final String TAG = "EditQuizAdapter";

    @NonNull
    @Override
    public EditQuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EditQuizViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reports_edit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EditQuizViewHolder holder, int position) {
        strQuiz = list.get(position).getQuiz();
        strOpn1 = list.get(position).getOpt1();
        strOpn2 = list.get(position).getOpt2();
        strOpn3 = list.get(position).getOpt3();
        strOpn4 = list.get(position).getOpt4();

        holder.etQuizQuestionEdit.setText(strQuiz);
        holder.etQuizOP1Edit.setText(strOpn1);
        holder.etQuizOP2Edit.setText(strOpn2);
        holder.etQuizOP3Edit.setText(strOpn3);
        holder.etQuizOP4Edit.setText(strOpn4);

        correctOpn = list.get(position).getCorrectOpn();

        if (correctOpn == 1) {
            holder.cbQuizAEdit.setChecked(true);
            holder.cbQuizBEdit.setChecked(false);
            holder.cbQuizCEdit.setChecked(false);
            holder.cbQuizDEdit.setChecked(false);
        } else if (correctOpn == 2) {
            holder.cbQuizAEdit.setChecked(false);
            holder.cbQuizBEdit.setChecked(true);
            holder.cbQuizCEdit.setChecked(false);
            holder.cbQuizDEdit.setChecked(false);

        } else if (correctOpn == 3) {
            holder.cbQuizAEdit.setChecked(false);
            holder.cbQuizBEdit.setChecked(false);
            holder.cbQuizCEdit.setChecked(true);
            holder.cbQuizDEdit.setChecked(false);

        } else if (correctOpn == 4) {
            holder.cbQuizAEdit.setChecked(false);
            holder.cbQuizBEdit.setChecked(false);
            holder.cbQuizCEdit.setChecked(false);
            holder.cbQuizDEdit.setChecked(true);

        }

        holder.cbQuizAEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cbQuizAEdit.setChecked(true);
                holder.cbQuizBEdit.setChecked(false);
                holder.cbQuizCEdit.setChecked(false);
                holder.cbQuizDEdit.setChecked(false);
            }
        });
        holder.cbQuizBEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cbQuizAEdit.setChecked(false);
                holder.cbQuizBEdit.setChecked(true);
                holder.cbQuizCEdit.setChecked(false);
                holder.cbQuizDEdit.setChecked(false);

            }
        });
        holder.cbQuizCEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cbQuizAEdit.setChecked(false);
                holder.cbQuizBEdit.setChecked(false);
                holder.cbQuizCEdit.setChecked(true);
                holder.cbQuizDEdit.setChecked(false);

            }
        });
        holder.cbQuizDEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cbQuizAEdit.setChecked(false);
                holder.cbQuizBEdit.setChecked(false);
                holder.cbQuizCEdit.setChecked(false);
                holder.cbQuizDEdit.setChecked(true);

            }
        });

        holder.btnQuizSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentId = list.get(position).getId();
                if (holder.cbQuizAEdit.isChecked()) {

                    newCorrectOpn = 1;
                } else if (holder.cbQuizBEdit.isChecked()) {

                    newCorrectOpn = 2;

                } else if (holder.cbQuizCEdit.isChecked()) {

                    newCorrectOpn = 3;

                } else if (holder.cbQuizDEdit.isChecked()) {

                    newCorrectOpn = 4;

                }
                quizDatabase = QuizDatabase.getInstance(v.getContext());
                strNewQuiz = holder.etQuizQuestionEdit.getText().toString();
                strNewOpn1 = holder.etQuizOP1Edit.getText().toString();
                strNewOpn2 = holder.etQuizOP2Edit.getText().toString();
                strNewOpn3 = holder.etQuizOP3Edit.getText().toString();
                strNewOpn4 = holder.etQuizOP4Edit.getText().toString();
                quizDatabase.quizDao().updateQuiz(currentId, strNewQuiz, strNewOpn1, strNewOpn2, strNewOpn3, strNewOpn4, newCorrectOpn)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                Snackbar.make(v, "The Modification applied successfully", Snackbar.LENGTH_LONG).show();

                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                Log.d(TAG, "onError: mgd" + e);
                            }
                        });
            }
        });

        holder.btnQuizDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentId = list.get(position).getId();
                quizDatabase = QuizDatabase.getInstance(v.getContext());
                quizDatabase.quizDao().deleteQuiz(currentId);
                Snackbar.make(v, "Deleted Successfully", Snackbar.LENGTH_LONG).show();
                quizDatabase.quizDao().getQuestions().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<StudentsQuestions>>() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@io.reactivex.annotations.NonNull List<StudentsQuestions> studentsQuestions) {
                                list = studentsQuestions;
                                if (list.size() == 0) {
                                    list.clear();
                                    holder.linearEditReport.setVisibility(View.GONE);
                                    holder.tvNoQuizEditAdded.setVisibility(View.VISIBLE);
                                }
                                if (list.size() > 0) {
                                    holder.etQuizQuestionEdit.setText(strQuiz);
                                    holder.etQuizOP1Edit.setText(strOpn1);
                                    holder.etQuizOP2Edit.setText(strOpn2);
                                    holder.etQuizOP3Edit.setText(strOpn3);
                                    holder.etQuizOP4Edit.setText(strOpn4);

                                    correctOpn = list.get(position).getCorrectOpn();

                                    if (correctOpn == 1) {
                                        holder.cbQuizAEdit.setChecked(true);
                                        holder.cbQuizBEdit.setChecked(false);
                                        holder.cbQuizCEdit.setChecked(false);
                                        holder.cbQuizDEdit.setChecked(false);
                                        newCorrectOpn = 1;
                                    } else if (correctOpn == 2) {
                                        holder.cbQuizAEdit.setChecked(false);
                                        holder.cbQuizBEdit.setChecked(true);
                                        holder.cbQuizCEdit.setChecked(false);
                                        holder.cbQuizDEdit.setChecked(false);
                                        newCorrectOpn = 2;

                                    } else if (correctOpn == 3) {
                                        holder.cbQuizAEdit.setChecked(false);
                                        holder.cbQuizBEdit.setChecked(false);
                                        holder.cbQuizCEdit.setChecked(true);
                                        holder.cbQuizDEdit.setChecked(false);
                                        newCorrectOpn = 3;

                                    } else if (correctOpn == 4) {
                                        holder.cbQuizAEdit.setChecked(false);
                                        holder.cbQuizBEdit.setChecked(false);
                                        holder.cbQuizCEdit.setChecked(false);
                                        holder.cbQuizDEdit.setChecked(true);
                                        newCorrectOpn = 4;

                                    }


                                }
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                            }
                        });

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<StudentsQuestions> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class EditQuizViewHolder extends RecyclerView.ViewHolder {
        EditText etQuizQuestionEdit, etQuizOP1Edit, etQuizOP2Edit, etQuizOP3Edit, etQuizOP4Edit;
        TextView tvNoQuizEditAdded;
        CheckBox cbQuizAEdit, cbQuizBEdit, cbQuizCEdit, cbQuizDEdit;
        AppCompatButton btnQuizSaveEdit, btnQuizDelete;
        LinearLayout linearEditReport;

        public EditQuizViewHolder(@NonNull View itemView) {
            super(itemView);

            etQuizQuestionEdit = itemView.findViewById(R.id.etQuizQuestionEdit);
            tvNoQuizEditAdded = itemView.findViewById(R.id.tvNoQuizEditAdded);
            etQuizOP1Edit = itemView.findViewById(R.id.etQuizOP1Edit);
            etQuizOP2Edit = itemView.findViewById(R.id.etQuizOP2Edit);
            etQuizOP3Edit = itemView.findViewById(R.id.etQuizOP3Edit);
            etQuizOP4Edit = itemView.findViewById(R.id.etQuizOP4Edit);
            cbQuizAEdit = itemView.findViewById(R.id.cbQuizAEdit);
            cbQuizBEdit = itemView.findViewById(R.id.cbQuizBEdit);
            cbQuizCEdit = itemView.findViewById(R.id.cbQuizCEdit);
            cbQuizDEdit = itemView.findViewById(R.id.cbQuizDEdit);
            btnQuizSaveEdit = itemView.findViewById(R.id.btnQuizSaveEdit);
            btnQuizDelete = itemView.findViewById(R.id.btnQuizDelete);
            linearEditReport = itemView.findViewById(R.id.linearEditReport);
        }
    }

}