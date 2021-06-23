package developer007.magdy.myquiz.database.quiz;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = StudentsQuestions.class, version = 2, exportSchema = false)
public abstract class QuizDatabase extends RoomDatabase {
    private static QuizDatabase instance;

    public abstract QuizDao quizDao();

    public static synchronized QuizDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), QuizDatabase.class,
                    "quiz_database").fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
}
