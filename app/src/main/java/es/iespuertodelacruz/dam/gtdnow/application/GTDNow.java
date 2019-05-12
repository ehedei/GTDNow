package es.iespuertodelacruz.dam.gtdnow.application;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GTDNow extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder().name("GTDNow.realm").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }


}
