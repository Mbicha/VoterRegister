package droid.f.voterregister.databaseutil;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Voter.class}, version = 1, exportSchema = false)
public abstract class VoterDatabase extends RoomDatabase {
    public abstract VoterDao voterDao();
    private static VoterDatabase INSTANCE;
    public static VoterDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (VoterDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VoterDatabase.class, "voter.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
