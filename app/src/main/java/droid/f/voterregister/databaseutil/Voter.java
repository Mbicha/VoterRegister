package droid.f.voterregister.databaseutil;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "voter_register")
public class Voter {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "voter_name")
    private String voterName;

    @ColumnInfo(name = "voter_station")
    private String voterStaion;

    @ColumnInfo(name = "voter_id")
    private int voterId;

    public Voter(String voterName, String voterStaion, int voterId) {
        this.voterName = voterName;
        this.voterStaion = voterStaion;
        this.voterId = voterId;
    }

    @Ignore
    public Voter(int id, String voterName, String voterStaion, int voterId) {
        this.id = id;
        this.voterName = voterName;
        this.voterStaion = voterStaion;
        this.voterId = voterId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public String getVoterStaion() {
        return voterStaion;
    }

    public void setVoterStaion(String voterStaion) {
        this.voterStaion = voterStaion;
    }

    public int getVoterId() {
        return voterId;
    }

    public void setVoterId(int voterId) {
        this.voterId = voterId;
    }
}
