package com.bh90210.cp500;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.RequiresApi;

import cpfiveoo.Cpfiveoo;

import static cpfiveoo.Cpfiveoo.dbView;

public class Util {
    // schedule the start of the service every 10 - 30 seconds
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context, long mili, String id) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);

        int jobID = View.generateViewId();
        Cpfiveoo.dbUpdate(String.format("%s_HELPER", id), String.valueOf(jobID));

        JobInfo.Builder builder = new JobInfo.Builder(jobID, serviceComponent);
        builder.setMinimumLatency(mili); // wait at least
        builder.setOverrideDeadline(1000 * 1000); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        builder.setPersisted(true);

        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("id",id);
        builder.setExtras(bundle);

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

}
