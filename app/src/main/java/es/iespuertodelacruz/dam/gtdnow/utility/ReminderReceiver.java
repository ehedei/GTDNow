package es.iespuertodelacruz.dam.gtdnow.utility;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.activity.DisplayerPlaceActivity;
import es.iespuertodelacruz.dam.gtdnow.activity.DisplayerTaskActivity;
import es.iespuertodelacruz.dam.gtdnow.application.GTDNow;


public class ReminderReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra(BundleHelper.TASK_NAME);
        int notificationId = intent.getStringExtra(BundleHelper.TASK_ID).hashCode();

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent i = new Intent(context, DisplayerTaskActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, GTDNow.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_task)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setSound(uri)
                .setContentTitle(context.getText(R.string.app_name))
                .setContentText(name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(notificationId, builder.build());
    }
}
