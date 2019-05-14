package com.bh90210.cp500;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cpfiveoo.Cpfiveoo;

public class ScrollingActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getApplicationContext();

        Cpfiveoo.initDBdirHelper(String.valueOf(context.getFilesDir())); // init the database passing the db dir

        createNotificationChannel();

        try {
            File file = new File(context.getFilesDir(), "MANIFEST");
            if (!file.exists()) {
                // LOGIC
                Resources r = getResources();
                InputStream mani = r.openRawResource(R.raw.manifest);
                InputStream sst = r.openRawResource(R.raw.sst);
                InputStream vlog = r.openRawResource(R.raw.vlog);
                byte[] buffermani = new byte[mani.available()];
                byte[] buffersst = new byte[sst.available()];
                byte[] buffervlog = new byte[vlog.available()];
                mani.read(buffermani);
                sst.read(buffersst);
                vlog.read(buffervlog);
                FileOutputStream outputStreammani;
                FileOutputStream outputStreamsst;
                FileOutputStream outputStreamvlog;
                try {
                    outputStreammani = openFileOutput("MANIFEST", Context.MODE_PRIVATE);
                    outputStreammani.write(buffermani);
                    outputStreammani.close();

                    outputStreamsst = openFileOutput("000004.sst", Context.MODE_PRIVATE);
                    outputStreamsst.write(buffersst);
                    outputStreamsst.close();

                    outputStreamvlog = openFileOutput("000000.vlog", Context.MODE_PRIVATE);
                    outputStreamvlog.write(buffervlog);
                    outputStreamvlog.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_scrolling);

        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                Permissions.check(ScrollingActivity.this, permissions, null, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(ScrollingActivity.this, PhotoPost.class);
                        startActivity(intent);
                    }
                });
            }
        });

        FloatingActionButton settings = (FloatingActionButton) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScrollingActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        //FloatingActionButton videobut = (FloatingActionButton) findViewById(R.id.videobut);
        //videobut.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Intent intent = new Intent(ScrollingActivity.this, Video.class);
        //        startActivity(intent);
        //    }
        //});

        int totalPosts = (int) Cpfiveoo.totalScheduledPosts();
        final TableLayout ll = findViewById(R.id.scheduled_posts);

        for(int i = 0; i < totalPosts; i ++) {

            final String id = Cpfiveoo.scheduledIds(i);
            String filepath = Cpfiveoo.dbView(String.format("%s_FILEPATH", id));

            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row,null);
            ll.addView(row,i);

            Bitmap myBitmap = BitmapFactory.decodeFile(filepath);
            ImageView schePre = row.findViewById(R.id.schePre);
            schePre.setImageBitmap(myBitmap);

            final View rowID = row.findViewById(R.id.tableraw);
            //Cpfiveoo.dbUpdate("ROW_ID_HELPER_"+id, String.valueOf(rowID));

            final TextView timeLeft = row.findViewById(R.id.timeLeft);
            String strtoint = Cpfiveoo.dbView("TIMER_HELPER_"+id);
            final long scheduledTimeInMili = Long.parseLong(strtoint);
            final long now = System.currentTimeMillis();
            final long timeleft = scheduledTimeInMili - now;
            final CountDownTimer countDownTimer = new CountDownTimer(timeleft, 60000) {
                public void onTick(long scheinmil) {
                    long timeinseconds = scheinmil/1000;
                    long timeinminutes = timeinseconds/60;
                    timeLeft.setText(String.valueOf(timeinminutes));
                }

                public void onFinish() {
                    Cpfiveoo.delHelper("TIMER_HELPER_"+id);
                    ll.removeView(rowID);
                }
            }.start();

            final Button pm = row.findViewById(R.id.pn);
            pm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // delete job
                    String idToInt = Cpfiveoo.dbView(String.format("%s_HELPER", id));
                    int idtodel = Integer.parseInt(idToInt);
                    JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;
                    scheduler.cancel(idtodel);
                    // delete db helpers
                    Cpfiveoo.delHelper(String.format("%s_HELPER", id));
                    Cpfiveoo.delHelper("TIMER_HELPER_"+id);
                    // draw snackbar
                    View snackk = findViewById(R.id.app_bar);
                    final Snackbar snackbar = Snackbar.make(snackk, "Uploading", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    // remove row
                    ll.removeView(rowID);
                    // cancel timer
                    countDownTimer.cancel();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Cpfiveoo.scheduledPostNow(id);
                            // send notification when done
                            showNotification();
                        }
                    }).start();
                }
            });

            Button del = row.findViewById(R.id.del);
            //final TableRow rowID = row.findViewById(R.id.tableraw);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete db entry
                    Cpfiveoo.deletePost(id);
                    // delete job
                    String idToInt = Cpfiveoo.dbView(String.format("%s_HELPER", id));
                    int idtodel = Integer.parseInt(idToInt);
                    JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;
                    scheduler.cancel(idtodel);
                    // delete db helpers
                    Cpfiveoo.delHelper(String.format("%s_HELPER", id));
                    Cpfiveoo.delHelper("TIMER_HELPER_"+id);
                    // draw snackbar
                    View snackk = findViewById(R.id.app_bar);
                    final Snackbar snackbar = Snackbar.make(snackk, "Deleted", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    // remove row
                    ll.removeView(rowID);
                    // calncel timer
                    countDownTimer.cancel();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
         //   goToSettings();
         //   return true;
        //}
        return super.onOptionsItemSelected(item);
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Upload", "Upload", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Posts upload notification");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //@Override
    /*public void updateClient(String id) {
        String dbReslt = Cpfiveoo.dbView("ROW_ID_HELPER_"+id);
        int rowID = Integer.parseInt(dbReslt);
        //View rowID = Cpfiveoo.dbView("ROW_ID_HELPER_"+id);
        final TableLayout ll = findViewById(R.id.scheduled_posts);
        final View row = findViewById(rowID);
        ll.removeView(row);
        Cpfiveoo.delHelper("ROW_ID_HELPER_"+id);
    }*/
}
