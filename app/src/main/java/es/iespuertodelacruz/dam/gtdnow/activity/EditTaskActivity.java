package es.iespuertodelacruz.dam.gtdnow.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import es.iespuertodelacruz.dam.gtdnow.R;
import io.realm.Realm;

public class EditTaskActivity extends AppCompatActivity {
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        //prepareActivity();

        Intent i = getIntent();

        if (savedInstanceState == null) {
            setTitle(R.string.edittask_title_create);
        } else {
            setTitle(R.string.edittask_title_edit);
        }

    }
}
