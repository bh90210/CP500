package com.bh90210.cp500;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;

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

import static com.bh90210.cp500.R.layout.activity_scrolling;

public class ScrollingActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        Cpfiveoo.openDB(String.valueOf(context.getFilesDir())); // init the database passing the db dir
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

        setContentView(activity_scrolling);

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

        //Intent intent = getIntent();
        //String ison = intent.getStringExtra("alarm");
        //String hour = intent.getStringExtra("hour");
        //String minute = intent.getStringExtra("minute");

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
            final Button pm = row.findViewById(R.id.pn);
            pm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Cpfiveoo.scheduledPostNow(id);
                            // send notification when done
                            showNotification();
                        }
                    }).start();

                    // delete job

                    // draw snackbar
                    View snackk = findViewById(R.id.app_bar);
                    final Snackbar snackbar = Snackbar.make(snackk, "Uploading", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    // remove row
                    ll.removeView(rowID);
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

                    // draw snackbar
                    View snackk = findViewById(R.id.app_bar);
                    final Snackbar snackbar = Snackbar.make(snackk, "Deleted", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    // remove row
                    ll.removeView(rowID);
                }
            });
        }
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        //return true;
    //}

    //public void goToSettings() {
    //    Intent intent = new Intent(this, Settings.class);
    //    startActivity(intent);
    //}

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cpfiveoo.closeDB();
    }
}
