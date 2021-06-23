package developer007.magdy.myquiz.database.student;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = StudentAnswer.class, version = 2, exportSchema = false)
public abstract class StudentAnswerDatabase extends RoomDatabase {
    private static StudentAnswerDatabase instance;

    public abstract StudentDao studentDao();

    public static synchronized StudentAnswerDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), StudentAnswerDatabase.class,
                    "answer_database").fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
}
