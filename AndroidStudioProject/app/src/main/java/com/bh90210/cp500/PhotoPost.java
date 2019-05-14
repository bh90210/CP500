package com.bh90210.cp500;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.Objects;

import cpfiveoo.Cpfiveoo;

public class PhotoPost extends AppCompatActivity {
    String filePathHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        setContentView(R.layout.activity_photo_post);
        setTitle("New Photo Post");
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Button savehashesPh = (Button) findViewById(R.id.savehashes);
        savehashesPh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText twHash = (TextInputEditText)findViewById(R.id.twHash);
                TextInputEditText inHash = (TextInputEditText)findViewById(R.id.inHash);
                Cpfiveoo.dbUpdate("twHash", String.valueOf(twHash.getText()));
                Cpfiveoo.dbUpdate("inHash", String.valueOf(inHash.getText()));
            }
        });
        String twHashValue = Cpfiveoo.dbView("twHash");
        TextInputEditText twHash = (TextInputEditText)findViewById(R.id.twHash);
        twHash.setText(twHashValue);
        String inHashValue = Cpfiveoo.dbView("inHash");
        TextInputEditText inHash = (TextInputEditText)findViewById(R.id.inHash);
        inHash.setText(inHashValue);
        final String fbCh = Cpfiveoo.dbView("fbCheckedMem");
        final CheckBox checkBoxfb = (CheckBox) findViewById(R.id.fbCheckButton);
        checkBoxfb.setChecked(Boolean.parseBoolean(fbCh));
        checkBoxfb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String fbi = Cpfiveoo.dbView("fbID");
                if (Objects.equals("", fbi)) {
                    checkBoxfb.setChecked(false);
                    // snackbar
                    Snackbar snackbar = Snackbar.make(buttonView, "Set your Facebook ID", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    String fbt = Cpfiveoo.dbView("fbTOKEN");
                    if (Objects.equals("", fbt)) {
                        checkBoxfb.setChecked(false);
                        // snackbar
                        Snackbar snackbar = Snackbar.make(buttonView, "Set your Facebook TOKEN", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        Cpfiveoo.dbUpdate("fbCheckedMem", String.valueOf(isChecked));
                    }
                }
            }
        });

        final String twCh = Cpfiveoo.dbView("twCheckedMem");
        final CheckBox checkBoxtw = (CheckBox) findViewById(R.id.twCheckButton);
        checkBoxtw.setChecked(Boolean.parseBoolean(twCh));
        checkBoxtw.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String twia = Cpfiveoo.dbView("twAPI");
                if (Objects.equals("", twia)) {
                    checkBoxtw.setChecked(false);
                    // snackbar
                    Snackbar snackbar = Snackbar.make(buttonView, "Set your Twitter API", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    String twias = Cpfiveoo.dbView("twAPIsec");
                    if (Objects.equals("", twias)) {
                        checkBoxtw.setChecked(false);
                        // snackbar
                        Snackbar snackbar = Snackbar.make(buttonView, "Set your Twitter API Secret", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        String twit = Cpfiveoo.dbView("twTOKEN");
                        if (Objects.equals("", twit)) {
                            checkBoxtw.setChecked(false);
                            // snackbar
                            Snackbar snackbar = Snackbar.make(buttonView, "Set your Twitter API Token", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            View snackbarView = snackbar.getView();
                            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        } else {
                            String twits = Cpfiveoo.dbView("twTOKENsec");
                            if (Objects.equals("", twits)) {
                                checkBoxtw.setChecked(false);
                                // snackbar
                                Snackbar snackbar = Snackbar.make(buttonView, "Set your Twitter API Token Secret", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                View snackbarView = snackbar.getView();
                                snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            } else {
                                Cpfiveoo.dbUpdate("twCheckedMem", String.valueOf(isChecked));
                            }
                        }
                    }
                }
            }
        });

        final String inCh = Cpfiveoo.dbView("inCheckedMem");
        final CheckBox checkBoxin = (CheckBox) findViewById(R.id.inCheckButton);
        checkBoxin.setChecked(Boolean.parseBoolean(inCh));
        checkBoxin.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String inu = Cpfiveoo.dbView("inUSER");
                if (Objects.equals("", inu)) {
                    checkBoxin.setChecked(false);
                    // snackbar
                    Snackbar snackbar = Snackbar.make(buttonView, "Set your Instagram Username", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    String inp = Cpfiveoo.dbView("inPASS");
                    if (Objects.equals("", inp)) {
                        checkBoxin.setChecked(false);
                        // snackbar
                        Snackbar snackbar = Snackbar.make(buttonView, "Set your Instagram Password", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        Cpfiveoo.dbUpdate("inCheckedMem", String.valueOf(isChecked));
                    }
                }
            }
        });

        ImageButton imageselect = (ImageButton) findViewById(R.id.photopreview);
        imageselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //<color name="colorPrimary">#99FF99</color
                // <color name="colorPrimaryDark">#6699FF</color>
    //<color name="colorAccent">#FFFF66</color>
                ImagePicker.with(PhotoPost.this)                         //  Initialize ImagePicker with activity or fragment context
                        .setToolbarColor("#99FF99")         //  Toolbar color
                        .setStatusBarColor("#6699FF")       //  StatusBar color (works with SDK >= 21  )
                        .setToolbarTextColor("#6699FF")     //  Toolbar text color (Title and Done button)
                        .setToolbarIconColor("#6699FF")     //  Toolbar icon color (Back and Camera button)
                        .setProgressBarColor("#6699FF")     //  ProgressBar color
                        .setBackgroundColor("#6699FF")      //  Background color
                        .setNavigationBarColor("#6699FF")   //  NavigationBar color
                        .setCameraOnly(false)               //  Camera mode
                        .setMultipleMode(false)              //  Select multiple images or single image
                        .setFolderMode(true)                //  Folder mode
                        .setShowCamera(true)                //  Show camera button
                        .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                        .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                        .setDoneTitle("Done")               //  Done button title
                        .setLimitMessage("You have reached selection limit")    // Selection limit message
                        .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                        .setRequestCode(100)                //  Set request code, default Config.RC_PICK_IMAGES
                        .setKeepScreenOn(true)              //  Keep screen on when selecting images
                        .start();
            }
        });

        Button schedule = findViewById(R.id.schedule);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean aB = Boolean.parseBoolean(Cpfiveoo.dbView("fbCheckedMem"));
                boolean bB = Boolean.parseBoolean(Cpfiveoo.dbView("twCheckedMem"));
                boolean cB = Boolean.parseBoolean(Cpfiveoo.dbView("inCheckedMem"));

                if (filePathHelper == null) {
                    // snackbar
                    Snackbar snackbar = Snackbar.make(v, "Select an image", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    if (!aB && !bB && !cB){
                        Snackbar snackbar = Snackbar.make(v, "Select at least one platform", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        Calendar mcurrentTime = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            mcurrentTime = Calendar.getInstance();
                        }
                        int hour = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        }
                        int minute = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            minute = mcurrentTime.get(Calendar.MINUTE);
                        }

                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(PhotoPost.this, new TimePickerDialog.OnTimeSetListener() {
                            @TargetApi(Build.VERSION_CODES.N)
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                // logic
                                TextInputEditText text = findViewById(R.id.text);
                                TextInputEditText twHash = findViewById(R.id.twHash);
                                TextInputEditText inHash = findViewById(R.id.inHash);
                                final String id = Cpfiveoo.schedule(String.valueOf(text.getText()), String.valueOf(twHash.getText()), String.valueOf(inHash.getText()), filePathHelper);

                                //make calendar to mili
                                Calendar calendar = Calendar.getInstance();
                                Calendar that = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                calendar.set(Calendar.MINUTE, selectedMinute);
                                if (calendar.getTimeInMillis() <= that.getTimeInMillis()) {
                                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH + 1));
                                }
                                long thiss = calendar.getTimeInMillis();
                                long mili = thiss - that.getTimeInMillis();

                                Util.scheduleJob(getApplicationContext(), mili, id);
                                Cpfiveoo.dbUpdate("TIMER_HELPER_"+id, String.valueOf(thiss));

                                Intent intent = new Intent(PhotoPost.this, ScrollingActivity.class);
                                startActivity(intent);
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.show();
                    }
                }
            }
        });

        Button postnoww = (Button) findViewById(R.id.postnow);
        postnoww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean aB = Boolean.parseBoolean(Cpfiveoo.dbView("fbCheckedMem"));
                boolean bB = Boolean.parseBoolean(Cpfiveoo.dbView("twCheckedMem"));
                boolean cB = Boolean.parseBoolean(Cpfiveoo.dbView("inCheckedMem"));

                if (filePathHelper == null) {
                    // snackbar
                    Snackbar snackbar = Snackbar.make(v, "Select an image", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    if (!aB && !bB && !cB){
                        Snackbar snackbar = Snackbar.make(v, "Select at least one platform", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                TextInputEditText text = (TextInputEditText)findViewById(R.id.text);
                                TextInputEditText twHash = (TextInputEditText)findViewById(R.id.twHash);
                                TextInputEditText inHash = (TextInputEditText)findViewById(R.id.inHash);
                                Cpfiveoo.postNow(String.valueOf(text.getText()), String.valueOf(twHash.getText()), String.valueOf(inHash.getText()), filePathHelper);
                                showNotification();
                            }
                        }).start();

                        Intent intent = new Intent(PhotoPost.this, ScrollingActivity.class);
                        startActivity(intent);

                        // draw waiting animation
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> list = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            // do your logic here...
            ImageButton loadImage = (ImageButton) findViewById(R.id.photopreview);
            Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(list.get(0).getPath()));
            loadImage.setImageBitmap(myBitmap);
            filePathHelper = String.valueOf(list.get(0).getPath());
        }
        super.onActivityResult(requestCode, resultCode, data);  // You MUST have this line to be here
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
