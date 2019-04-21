package es.iespuertodelacruz.dam.gtdnow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        if (savedInstanceState == null) {
            setTitle(R.string.edittask_title_create);
        } else {
            setTitle(R.string.edittask_title_edit);
        }

    }
}
