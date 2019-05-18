package es.iespuertodelacruz.dam.gtdnow.activity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.GroupDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.PlaceDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.ProjectDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.TaskDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import io.realm.Realm;

public class EditTaskActivity extends AppCompatActivity {
    private Realm realm;
    private Task task;
    private EditText editTextName;
    private Switch switchIsEnded;
    private EditText editTextPlace;
    private ImageButton imageButtonPlace;
    private EditText editTextProject;
    private ImageButton imageButtonProject;
    private EditText editTextEndTime;
    private ImageButton imageButtonEndTime;
    private EditText editTextReminder;
    private ImageButton imageButtonReminder;
    private SimpleDateFormat dateFormat;
    private Button buttonCancel;
    private Button buttonSave;
    private TaskDao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy '@' HH:mm:ss");
        editTextName = findViewById(R.id.edittext_edittask_name);
        switchIsEnded = findViewById(R.id.switch_edittask_isended);
        editTextPlace = findViewById(R.id.edittext_edittask_place);
        imageButtonPlace = findViewById(R.id.imagebutton_edittask_place);
        editTextProject = findViewById(R.id.edittext_edittask_project);
        imageButtonProject = findViewById(R.id.imagebutton_edittask_project);
        editTextEndTime = findViewById(R.id.edittext_edittask_deadline);
        imageButtonEndTime = findViewById(R.id.imagebutton_edittask_deadline);
        editTextReminder = findViewById(R.id.edittext_edittask_reminder);
        imageButtonReminder = findViewById(R.id.imagebutton_edittask_reminder);
        buttonCancel = findViewById(R.id.button_edittask_back);
        buttonSave = findViewById(R.id.button_edittask_save);

        realm = Realm.getDefaultInstance();
        taskDao = new TaskDao();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSaveTask();
            }
        });

        editTextPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DisplayerPlaceActivity.class);
                startActivityForResult(intent, BundleHelper.PLACE_ACTIVITY);
            }
        });
        prepareTask();
    }

    private void prepareTask() {
        Intent i = getIntent();
        String taskId = i.getStringExtra(BundleHelper.TASK_ID);
        String projectId = i.getStringExtra(BundleHelper.PROJECT_ID);
        String placeId = i.getStringExtra(BundleHelper.PLACE_ID);
        String groupId = i.getStringExtra(BundleHelper.GROUP_ID);

        if (taskId == null) {
            task = new Task();
            setTitle(R.string.edittask_title_create);
        }
        else {
            task = taskDao.getTaskById(taskId).clone();
            setTitle(R.string.edittask_title_edit);
        }

        if (groupId != null) {
            task.getGroups().add(new GroupDao().getGroupById(groupId));
        }

        if (projectId != null) {
            task.setProject(new ProjectDao().getProjectById(projectId));
        }

        if (placeId != null) {
            task.setPlace(new PlaceDao().getPlaceById(projectId));
        }

        fillTaskFields();

    }

    private void fillTaskFields() {
        if (task.getName() != null)
            editTextName.setText(task.getName());
        else
            editTextName.setText("");

        switchIsEnded.setChecked(task.isCompleted());

        if (task.getPlace() != null) {
            editTextPlace.setText(task.getPlace().getName());
            imageButtonPlace.setVisibility(View.VISIBLE);
            imageButtonPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    task.setPlace(null);
                    fillTaskFields();
                }
            });
        }
        else {
            editTextPlace.setText("");
            imageButtonPlace.setVisibility(View.GONE);
            imageButtonPlace.setOnClickListener(null);
        }

        if (task.getProject() != null) {
            editTextProject.setText(task.getProject().getName());
            imageButtonProject.setVisibility(View.VISIBLE);
            imageButtonProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    task.setProject(null);
                    fillTaskFields();
                }
            });
        }
        else {
            editTextProject.setText("");
            imageButtonProject.setVisibility(View.GONE);
            imageButtonProject.setOnClickListener(null);
        }

        if (task.getEndTime() != null) {
            editTextEndTime.setText(dateFormat.format(task.getEndTime()));
            imageButtonEndTime.setVisibility(View.VISIBLE);
            imageButtonEndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    task.setEndTime(null);
                    fillTaskFields();
                }
            });

        }
        else {
            editTextEndTime.setText("");
            imageButtonEndTime.setVisibility(View.GONE);
            imageButtonEndTime.setOnClickListener(null);
        }


        if (task.getReminder() != null) {
            editTextReminder.setText(task.getReminder());
            imageButtonReminder.setVisibility(View.VISIBLE);
            imageButtonReminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    task.setReminder(null);
                    fillTaskFields();
                }
            });

        }
        else {
            editTextReminder.setText("");
            imageButtonReminder.setVisibility(View.GONE);
            imageButtonReminder.setOnClickListener(null);
        }

    }

    private void validateAndSaveTask() {
        String name = editTextName.getText().toString().trim();
        if (name.isEmpty())
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.message_name_required), Toast.LENGTH_SHORT).show();
        else {
            task.setName(name);
            taskDao.createOrUpdateTask(task);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == BundleHelper.PLACE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String placeId = intent.getStringExtra(BundleHelper.PLACE_ID);
                Place place = realm.where(Place.class).equalTo("placeId", placeId).findFirst();
                task.setPlace(place);
                editTextPlace.setText(place.getName());
            }
        }
    }

    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
