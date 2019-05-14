package com.bh90210.cp500;

import android.app.Activity;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PersistableBundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import cpfiveoo.Cpfiveoo;

public class TestJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        PersistableBundle pb=params.getExtras();
        String id = pb.getString("id");

        Intent service = new Intent(getApplicationContext(), ScrollingActivity.class);
        //service.putExtra("deleteRow", id);
        getApplicationContext().startService(service);
        //LocalBroadcastManager.getInstance(this).sendBroadcast(service);

        Context context = getApplicationContext();
        Cpfiveoo.initDBdirHelper(String.valueOf(context.getFilesDir())); // init the database passing the db dir
        Cpfiveoo.scheduledPostUpload(id);
        showNotification();
        Cpfiveoo.delHelper(String.format("%s_HELPER", id));

        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    public void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Upload")
                .setSmallIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
                .setContentTitle("Message from CP500")
                .setContentText("Your photo was succesufully uploaded?!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }
}