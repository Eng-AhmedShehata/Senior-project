package qu.edu.qa.seniorproject.localDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM Location")
    LiveData<List<Location>> getAllLocation();

    @Query("SELECT * FROM Location ORDER BY fav DESC")
    LiveData<List<Location>> getAllFavLocation();

    @Insert
    void insertAllLocation(List<Location> mList);

    @Update
    void updateFavNum(Location location);
}
