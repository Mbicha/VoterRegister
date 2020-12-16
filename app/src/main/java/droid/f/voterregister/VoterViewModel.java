package droid.f.voterregister;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import droid.f.voterregister.databaseutil.Voter;

public class VoterViewModel extends AndroidViewModel {
    private VoterRepository mRepository;
    LiveData<List<Voter>> mAllVoters;

    public VoterViewModel(Application application){
        super(application);
        mRepository = new VoterRepository(application);
        mAllVoters = mRepository.getAllVoters();
    }
    public LiveData<List<Voter>> getAllVoters() {
        return mAllVoters;
    }

    public void insert(Voter voter){
        mRepository.insert(voter);
    }

    public void deleteVoter(Voter voter){
        mRepository.deleteVoter(voter);
    }

    public void updateVoter(Voter voter){mRepository.updateVoter(voter);}

    public void deleteAllVoters(){mRepository.deleteAllVoters();}
}
