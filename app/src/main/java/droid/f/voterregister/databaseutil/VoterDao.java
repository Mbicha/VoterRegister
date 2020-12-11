package droid.f.voterregister.databaseutil;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VoterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Voter voter);

    @Delete
    void deleteVoter(Voter voter);

    @Update
    void updateVoter(Voter voter);

    @Query("SELECT * FROM voter_register")
    LiveData<List<Voter>> getAllVoters();

    @Query("DELETE FROM voter_register")
    void  deleteAll();
}
