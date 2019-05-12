package es.iespuertodelacruz.dam.gtdnow.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import io.realm.Realm;
import io.realm.RealmResults;

public class EditGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_edit_simple);

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Group> groups = realm.where(Group.class).findAll();

        for (Group g: groups) {
            Toast.makeText(getApplicationContext(), g.getName(), Toast.LENGTH_SHORT).show();
        }


    }
}
