package droid.f.voterregister.worknotificationmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import droid.f.voterregister.R;


public class VoterNotification extends Worker {
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private static final String  CHANNEL_NAME = "Voter Registration";
    private static final int NOTIFICACTION_ID = 3;

    private NotificationManager mNotificationManager;

    public VoterNotification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        showVoterNotification();
        return Result.success();
    }

    private void showVoterNotification(){
        mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createVoterNotificationChannel();
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createVoterNotificationChannel() {
        NotificationChannel voterNotifiactionChannel = new
                NotificationChannel(PRIMARY_CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        mNotificationManager.createNotificationChannel(voterNotifiactionChannel);


        NotificationCompat.Builder voterNotification = new
                NotificationCompat.Builder(getApplicationContext(), PRIMARY_CHANNEL_ID)
                .setContentTitle(CHANNEL_NAME)
                .setContentText("One voter added in the register")
                .setSmallIcon(R.drawable.registervotersmallpicture);
        mNotificationManager.notify(NOTIFICACTION_ID, voterNotification.build());
    }
}
