package droid.f.voterregister;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import droid.f.voterregister.databaseutil.Voter;
import droid.f.voterregister.databaseutil.VoterDao;
import droid.f.voterregister.databaseutil.VoterDatabase;

public class VoterRepository {
    private VoterDao mVoterDao;
    private LiveData<List<Voter>> mAllVoters;

    VoterRepository(Application application){
        VoterDatabase db = VoterDatabase.getDatabase(application);
        mVoterDao = db.voterDao();
        mAllVoters = mVoterDao.getAllVoters();
    }

    LiveData<List<Voter>> getAllVoters(){
        return mAllVoters;
    }

    public void insert(Voter voter){
        InsertAsync insertAsync = new InsertAsync(mVoterDao);
        insertAsync.execute(voter);
    }
    public void deleteVoter(Voter voter){
        DeleteVoterAsycnTask deleteVoterAsycnTask = new DeleteVoterAsycnTask(mVoterDao);
        deleteVoterAsycnTask.execute(voter);
    }

    public void updateVoter(Voter voter){
        UpdateVoterAsyncTask updateVoterAsyncTask = new UpdateVoterAsyncTask(mVoterDao);
        updateVoterAsyncTask.execute(voter);
    }

    public void deleteAllVoters(){
        DeleteAllVoterAsyncTask deleteAllVoterAsync = new DeleteAllVoterAsyncTask(mVoterDao);
        deleteAllVoterAsync.execute();
    }
    /*
    *Async task for every operation in the database
     */
    private static class InsertAsync extends AsyncTask<Voter, Void, Void>{
        private VoterDao mVoterDao;

        public InsertAsync(VoterDao mVoterDao) {
            this.mVoterDao = mVoterDao;
        }

        @Override
        protected Void doInBackground(Voter... voters) {
            mVoterDao.insert(voters[0]);
            return null;
        }
    }

    private static class DeleteVoterAsycnTask extends AsyncTask<Voter, Void, Void>{
        private VoterDao mVoterDao;

        public DeleteVoterAsycnTask(VoterDao mVoterDao){
            this.mVoterDao = mVoterDao;
        }

        @Override
        protected Void doInBackground(Voter... voters) {
            mVoterDao.deleteVoter(voters[0]);
            return null;
        }
    }

    private static class UpdateVoterAsyncTask extends AsyncTask<Voter, Void, Void>{
        private VoterDao mVoterDao;

        public UpdateVoterAsyncTask(VoterDao voterDao) {
            mVoterDao = voterDao;
        }

        @Override
        protected Void doInBackground(Voter... voters) {
            mVoterDao.updateVoter(voters[0]);
            return null;
        }
    }

    private static class DeleteAllVoterAsyncTask extends AsyncTask<Void, Void, Void>{
        private VoterDao mVoterDao;

        public DeleteAllVoterAsyncTask(VoterDao voterDao) {
            mVoterDao = voterDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mVoterDao.deleteAll();
            return null;
        }
    }

}
