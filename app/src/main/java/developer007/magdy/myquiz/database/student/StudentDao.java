package developer007.magdy.myquiz.database.student;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAnswer(StudentAnswer studentAnswer);

    @Query("select * from student_answer")
    Single<List<StudentAnswer>> getReport();

}
