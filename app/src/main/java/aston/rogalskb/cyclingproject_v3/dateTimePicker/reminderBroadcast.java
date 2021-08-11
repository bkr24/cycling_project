package aston.rogalskb.cyclingproject_v3.dateTimePicker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import aston.rogalskb.cyclingproject_v3.R;

public class reminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String routeName = intent.getExtras().getString("routeName");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notifyRoute")
                .setSmallIcon(R.drawable.ic_alarm_add_black_24dp)
                .setContentTitle("Cycling reminder!")
                .setContentText("Hey cyclists, remember you scheduled a trip for " + routeName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200,builder.build());

    }
}
