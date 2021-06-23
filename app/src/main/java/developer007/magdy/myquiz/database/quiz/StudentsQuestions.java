package developer007.magdy.myquiz.database.quiz;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students_questions")
public class StudentsQuestions {

    @PrimaryKey(autoGenerate = true)
    int id;
    String quiz;
    String opt1;
    String opt2;
    String opt3;
    String opt4;
    int correctOpn;

    public StudentsQuestions(String quiz, String opt1, String opt2, String opt3, String opt4, int correctOpn) {

        this.quiz = quiz;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.opt4 = opt4;
        this.correctOpn = correctOpn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getOpt1() {
        return opt1;
    }

    public void setOpt1(String opt1) {
        this.opt1 = opt1;
    }

    public String getOpt2() {
        return opt2;
    }

    public void setOpt2(String opt2) {
        this.opt2 = opt2;
    }

    public String getOpt3() {
        return opt3;
    }

    public void setOpt3(String opt3) {
        this.opt3 = opt3;
    }

    public String getOpt4() {
        return opt4;
    }

    public void setOpt4(String opt4) {
        this.opt4 = opt4;
    }

    public int getCorrectOpn() {
        return correctOpn;
    }

    public void setCorrectOpn(int correctOpn) {
        this.correctOpn = correctOpn;
    }
}
