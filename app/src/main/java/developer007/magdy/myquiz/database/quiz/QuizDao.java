package developer007.magdy.myquiz.database.quiz;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertQuestion(StudentsQuestions studentsQuestions);

    @Query("select * from STUDENTS_QUESTIONS")
    Single<List<StudentsQuestions>> getQuestions();

    @Query("DELETE FROM STUDENTS_QUESTIONS WHERE id = :id")
    void deleteQuiz(int id);

    @Query("Update students_questions set quiz = :quiz, opt1 = :opt1, opt2 = :opt2, opt3 = :opt3, opt4 = :opt4, correctOpn = :correctOpn where id = :id")
    Completable updateQuiz(int id, String quiz, String opt1, String opt2, String opt3, String opt4, int correctOpn);


}
