package droid.f.voterregister;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import droid.f.voterregister.databaseutil.Voter;

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

            setResult(RESULT_OK, feedbackIntent);
            //notifyOnVoterRegistration();
            finish();
        }
    }

    private void notifyOnVoterRegistration() {
        NotificationCompat.Builder voterNotifybuilder = getVoterNotificationBuilder();
        mNotify.notify(NOTIFICACTION_ID, voterNotifybuilder.build());
    }

    public void createVoterNotificationChannel(){
        mNotify = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel voterNotificationChannel =
                    new NotificationChannel(PRIMARY_CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            voterNotificationChannel.enableVibration(true);
            voterNotificationChannel.setLightColor(Color.GREEN);
            voterNotificationChannel.enableLights(true);
            voterNotificationChannel.setDescription("Notification From Voter Registerer");
            mNotify.createNotificationChannel(voterNotificationChannel);
        }
    }

    private NotificationCompat.Builder getVoterNotificationBuilder(){
        NotificationCompat.Builder notifyVoterBuilder =
                new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                        .setContentTitle("New Voter registerd")
                        .setContentText("Check to confirm new Voter in our register")
                        .setSmallIcon(R.drawable.registervotersmallpicture);
        return notifyVoterBuilder;
    }
}