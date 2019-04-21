package es.iespuertodelacruz.dam.gtdnow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        if (savedInstanceState == null) {
            setTitle(R.string.editgroup_title_create);
        } else {
            setTitle(R.string.editgroup_title_edit);
        }

    }
}
