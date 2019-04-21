package es.iespuertodelacruz.dam.gtdnow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);

        if (savedInstanceState == null) {
            setTitle(R.string.editproject_title_create);
        } else {
            setTitle(R.string.editproject_title_edit);
        }

    }
}
