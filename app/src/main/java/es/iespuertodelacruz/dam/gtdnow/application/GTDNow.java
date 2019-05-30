package es.iespuertodelacruz.dam.gtdnow.application;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

import es.iespuertodelacruz.dam.gtdnow.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GTDNow extends Application {
    public static final String CHANNEL_ID = "es.iespuertodelacruz.dam.gtdnow";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder().name("GTDNow.realm").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        createNotificationChannel();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);


            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.WHITE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
