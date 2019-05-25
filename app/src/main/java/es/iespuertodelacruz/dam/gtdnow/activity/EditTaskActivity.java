package es.iespuertodelacruz.dam.gtdnow.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.GroupDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.PlaceDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.ProjectDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.TaskDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;
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
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy '@' HH:mm");
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
                Intent intent = new Intent(getApplicationContext(), SelectorPlaceActivity.class);
                startActivityForResult(intent, BundleHelper.PLACE_ACTIVITY);
            }
        });

        editTextProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectorProjectActivity.class);
                startActivityForResult(intent, BundleHelper.PROJECT_ACTIVITY);
            }
        });

        editTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                if (task.getEndTime() != null)
                    calendar.setTime(task.getEndTime());

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);

                        TimePickerDialog timePicker = new TimePickerDialog(EditTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                task.setEndTime(calendar.getTime());
                                fillTaskFields();
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                        timePicker.show();
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        prepareTask();
    }

    private void prepareTask() {
        Intent i = getIntent();

        mode = i.getIntExtra(BundleHelper.EDIT_TASK_MODE, BundleHelper.TASK_ALL);

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
        if (task.getName() != null && editTextName.getText().length() == 0)
            editTextName.setText(task.getName());

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
        setMode();
    }

    private void setMode() {
        switch (mode) {
            case BundleHelper.TASK_FROM_PROJECT:
                editTextProject.setEnabled(false);
                editTextProject.setClickable(false);
                imageButtonProject.setVisibility(View.GONE);
                break;

            case BundleHelper.TASK_FROM_PLACE:
                editTextPlace.setEnabled(false);
                editTextPlace.setClickable(false);
                imageButtonPlace.setVisibility(View.GONE);
                break;
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
                Place place = new PlaceDao().getPlaceById(placeId);
                task.setPlace(place);
                fillTaskFields();
            }
        }
        else if (requestCode == BundleHelper.PROJECT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String projectId = intent.getStringExtra(BundleHelper.PROJECT_ID);
                Project project = new ProjectDao().getProjectById(projectId);
                task.setProject(project);
                fillTaskFields();
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        editTextName.setText(savedInstanceState.getString(BundleHelper.NAME));
        task.setCompleted(savedInstanceState.getBoolean(BundleHelper.IS_ENDED));

        if (savedInstanceState.getLong(BundleHelper.DEADLINE) != Long.MIN_VALUE)
            task.setEndTime(new Date(savedInstanceState.getLong(BundleHelper.DEADLINE)));

        task.setProject(new ProjectDao().getProjectById(savedInstanceState.getString(BundleHelper.PROJECT_ID)));
        task.setPlace(new PlaceDao().getPlaceById(savedInstanceState.getString(BundleHelper.PLACE_ID)));

        //task.setReminder();

        fillTaskFields();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(BundleHelper.NAME, editTextName.getText().toString());
        outState.putBoolean(BundleHelper.IS_ENDED, task.isCompleted());

        if (task.getPlace() != null) {
            outState.putString(BundleHelper.PLACE_ID, task.getPlace().getPlaceId());
        }

        if (task.getProject() != null) {
            outState.putString(BundleHelper.PROJECT_ID, task.getProject().getProjectId());
        }

        if (task.getEndTime() != null)
            outState.putLong(BundleHelper.DEADLINE, task.getEndTime().getTime());
        else
            outState.putLong(BundleHelper.DEADLINE, Long.MIN_VALUE);

//        if (task.getReminder() != null) {
//
//        }

        super.onSaveInstanceState(outState);
    }




    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
