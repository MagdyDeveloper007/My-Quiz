package developer007.magdy.myquiz.database.report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import developer007.magdy.myquiz.R;
import developer007.magdy.myquiz.database.student.StudentAnswer;


public class StudentReportAdapter extends RecyclerView.Adapter<StudentReportAdapter.StudentReportViewHolder> {

    List<StudentAnswer> list = new ArrayList<>();


    @NonNull
    @Override
    public StudentReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentReportViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reports, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentReportViewHolder holder, int position) {

        holder.tvStudentName.setText(list.get(position).getName().substring(0, 8));
        holder.tvStudentScore.setText(list.get(position).getCorrectAnswer());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<StudentAnswer> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class StudentReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvStudentScore;

        public StudentReportViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentScore = itemView.findViewById(R.id.tvStudentScore);
        }
    }
}