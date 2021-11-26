package com.example.ohthmhyh;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBoradcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri soundUri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                "://"+context.
                getApplicationContext().getPackageName()+
                "/"+R.raw.audio);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"Make_Alarm")
                .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                .setContentTitle("!!!!HABITS!!!!")
                .setContentText("Get your Habits Done!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setSound(soundUri);
        }
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200,builder.build());
    }
}

