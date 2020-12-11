package droid.f.voterregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import droid.f.voterregister.adapters.VoterAdapter;
import droid.f.voterregister.databaseutil.Voter;

import static droid.f.voterregister.Constants.*;

public class AllVoters extends AppCompatActivity {

    private static final String TAG = AllVoters.class.getSimpleName();
    private VoterAdapter mVoterAdapter;
    private RecyclerView mRecyclerView;
    private VoterViewModel mVoterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.voter_title);
        mVoterViewModel = new ViewModelProvider(this).get(VoterViewModel.class);
        setUpRecyclerView();
        displayVoters();

    }

    private void displayVoters() {
        mVoterViewModel.getAllVoters().observe(this, votersList -> mVoterAdapter.setVoterAdapter(votersList));
    }

    private void setUpRecyclerView() {
        mRecyclerView = findViewById(R.id.rec_voter_view);
        mVoterAdapter = new VoterAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mVoterAdapter);

        editVoterData();
        deleteVoter();
    }

    //deletes one voter
    private void deleteVoter() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mVoterViewModel.deleteVoter(mVoterAdapter.getVoterPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_all:
                deleteAllVoters();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAllVoters() {
        mVoterViewModel.deleteAllVoters();
    }

    private void editVoterData() {
        mVoterAdapter.setOnClickListener(voter -> {
            Intent editIntent = new Intent(this, NewVoter.class);

            editIntent.putExtra(EXTRA_VOTER_ID, voter.getId());
            editIntent.putExtra(EXTRA_VOTER_NAME, voter.getVoterName());
            editIntent.putExtra(EXTRA_VOTER_STATION, voter.getVoterStaion());
            editIntent.putExtra(EXTRA_VOTER_IDNUM, voter.getVoterId());

            startActivityForResult(editIntent, UPDATE_VOTER_REQUEST);
        });
    }

    public void registerNewVoter(View view) {
        Intent intent = new Intent(this, NewVoter.class);
        startActivityForResult(intent, NEW_VOTER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_VOTER_REQUEST_CODE && resultCode == RESULT_OK){

            //get our intent extra
            String vName = data.getStringExtra(EXTRA_VOTER_NAME);
            String vStation = data.getStringExtra(EXTRA_VOTER_STATION);
            int vIdNumber = data.getIntExtra(EXTRA_VOTER_IDNUM, 0000000);

            Voter voter = new Voter(vName, vStation, vIdNumber);
            mVoterViewModel.insert(voter);

        }else if ((requestCode == UPDATE_VOTER_REQUEST && resultCode == RESULT_OK)){
            int id = data.getIntExtra(EXTRA_VOTER_ID, NO_POSITION);
            if (id == NO_POSITION){
                Toast.makeText(this, "Voter Update Failed!", Toast.LENGTH_SHORT).show();
            }else {
                //get our intent extra
                String vName = data.getStringExtra(EXTRA_VOTER_NAME);
                String vStation = data.getStringExtra(EXTRA_VOTER_STATION);
                int vIdNumber = data.getIntExtra(EXTRA_VOTER_IDNUM, 0000000);

                //Voter Object
                Voter voter = new Voter(vName, vStation, vIdNumber);
                voter.setId(id);
                mVoterViewModel.updateVoter(voter);

                Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Voter Not Registered!", Toast.LENGTH_SHORT).show();
        }
    }
}