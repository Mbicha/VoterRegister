package droid.f.voterregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.TimeUnit;

import droid.f.voterregister.databaseutil.Voter;
import droid.f.voterregister.worknotificationmanager.VoterNotification;

import static droid.f.voterregister.Constants.*;

public class NewVoter extends AppCompatActivity {

    private VoterViewModel mVoterViewModel;
    private EditText mVoterName;
    private EditText mVoterStation;
    private EditText mVoterIdNum;

    private NotificationManager mNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_voter);

        mVoterName = findViewById(R.id.text_voter_name);
        mVoterStation = findViewById(R.id.text_voter_station);
        mVoterIdNum = findViewById(R.id.text_voter_id);

        mVoterViewModel = new ViewModelProvider(this).get(VoterViewModel.class);
        FloatingActionButton fabRegisterVoter = findViewById(R.id.btn_save);
        fabRegisterVoter.setOnClickListener(view -> registerNewVoter());

        voterDataToEdit();
    }

    private void voterDataToEdit() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_VOTER_ID)){
            setTitle("Edit Voter");
            mVoterName.setText(intent.getStringExtra(EXTRA_VOTER_NAME));
            mVoterStation.setText(intent.getStringExtra(EXTRA_VOTER_STATION));
            mVoterIdNum.setText(Integer.toString(intent.getIntExtra(EXTRA_VOTER_IDNUM, 0)));
        }else {
            setTitle("Register New Employee");
        }
    }

    private void registerNewVoter() {
        String vName = mVoterName.getText().toString();
        String vStation = mVoterStation.getText().toString();
        int vIdNum = Integer.parseInt(mVoterIdNum.getText().toString());

        Intent feedbackIntent = new Intent();
        if (vName.trim().isEmpty() || vStation.trim().isEmpty()){
            setResult(RESULT_CANCELED, feedbackIntent);
            Toast.makeText(this, "One or More fields is empty", Toast.LENGTH_LONG).show();
        } else {
            feedbackIntent.putExtra(EXTRA_VOTER_NAME, vName);
            feedbackIntent.putExtra(EXTRA_VOTER_STATION, vStation);
            feedbackIntent.putExtra(EXTRA_VOTER_IDNUM, vIdNum);

            int id = getIntent().getIntExtra(EXTRA_VOTER_ID, NO_POSITION);
            if (id != NO_POSITION){
                feedbackIntent.putExtra(EXTRA_VOTER_ID, id);
            }

            notifyOnVoterRegistration();
            setResult(RESULT_OK, feedbackIntent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_voter_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_share:
                sendVoterInfo();
                break;
            case R.id.action_notify:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //invoke notification
    private void notifyOnVoterRegistration() {
        OneTimeWorkRequest oneTimeWorkRequest = new
                OneTimeWorkRequest.Builder(VoterNotification.class).setInitialDelay(5, TimeUnit.SECONDS).build();
        WorkManager.getInstance(this)
                .enqueue(oneTimeWorkRequest);
    }

    //sends voter data to the intended recipient via any of the installed apps
    private void sendVoterInfo() {
        String vName = mVoterName.getText().toString();
        String vStation = mVoterStation.getText().toString();

        String mimeType = "plain/text";
        String subject = "Confirmation of Registration";
        String body = "Hello " + vName +",This is to inform you that you are now a registered voter /n." +
                "Congratulations and enjoy your voting rights at " + vStation;

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share Voter Data:")
                .setSubject(subject)
                .setText(body)
                .startChooser();
    }

}