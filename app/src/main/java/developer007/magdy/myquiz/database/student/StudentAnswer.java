package developer007.magdy.myquiz.database.student;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_answer")
public class StudentAnswer {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String correctAnswer;

    public StudentAnswer(String name, String correctAnswer) {
        this.name = name;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
