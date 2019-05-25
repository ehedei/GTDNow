package es.iespuertodelacruz.dam.gtdnow.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.GroupDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.PlaceDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.ProjectDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.TaskDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import io.realm.Realm;

public class EditProjectActivity extends AppCompatActivity {
    private Project project;
    private ProjectDao projectDao;
    private Realm realm;
    private SimpleDateFormat dateFormat;
    private EditText projectName;
    private EditText deadline;
    private ImageButton deadlineButton;
    private Switch isEnded;
    private Button cancelButton;
    private Button saveButton;

    public EditProjectActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);
        projectName = findViewById(R.id.edittext_editproject_name);
        deadline = findViewById(R.id.edittext_editproject_deadline);
        deadlineButton = findViewById(R.id.imagebutton_editproject_deadline);
        isEnded = findViewById(R.id.switch_editproject_isended);
        cancelButton = findViewById(R.id.button_editproject_back);
        saveButton = findViewById(R.id.button_editproject_save);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy '@' HH:mm");

        realm = Realm.getDefaultInstance();

        projectDao = new ProjectDao();

        prepareProject();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = projectName.getText().toString().trim();
                if (name.isEmpty())
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.message_name_required), Toast.LENGTH_SHORT).show();
                else {
                    project.setName(name);
                    projectDao.createOrUpdateProject(project);
                    Intent intent = getIntent();
                    intent.putExtra(BundleHelper.PROJECT_ID, project.getProjectId());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        isEnded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                project.setCompleted(isChecked);
            }
        });

        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                if (project.getEndTime() != null)
                    calendar.setTime(project.getEndTime());

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(EditProjectActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);

                        TimePickerDialog timePicker = new TimePickerDialog(EditProjectActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                project.setEndTime(calendar.getTime());
                                fillTaskFields();
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                        timePicker.show();
                    }
                }, year, month, day);
                datePicker.show();


            }
        });

    }


    private void prepareProject() {
        Intent i = getIntent();
        String projectId = i.getStringExtra(BundleHelper.PROJECT_ID);
        String taskId = i.getStringExtra(BundleHelper.TASK_ID);


        if (projectId == null) {
            project = new Project();
            setTitle(R.string.editproject_title_create);
        }
        else {
            project = projectDao.getProjectById(projectId).clone();
            setTitle(R.string.editproject_title_edit);
        }

        if (taskId != null) {
            project.getTasks().add(new TaskDao().getTaskById(taskId));
        }

        fillTaskFields();

    }

    private void fillTaskFields() {
        if (project.getName() != null && projectName.getText().length() == 0)
            projectName.setText(project.getName());

        isEnded.setChecked(project.isCompleted());

        if (project.getEndTime() != null) {
            deadline.setText(dateFormat.format(project.getEndTime()));
            deadlineButton.setVisibility(View.VISIBLE);
            deadlineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    project.setEndTime(null);
                    fillTaskFields();
                }
            });

        }
        else {
            deadline.setText("");
            deadlineButton.setVisibility(View.GONE);
            deadlineButton.setOnClickListener(null);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        projectName.setText(savedInstanceState.getString(BundleHelper.NAME));
        project.setCompleted(savedInstanceState.getBoolean(BundleHelper.IS_ENDED));

        if (savedInstanceState.getLong(BundleHelper.DEADLINE) != Long.MIN_VALUE)
            project.setEndTime(new Date(savedInstanceState.getLong(BundleHelper.DEADLINE)));

        //task.setReminder();

        fillTaskFields();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(BundleHelper.NAME, projectName.getText().toString());
        outState.putBoolean(BundleHelper.IS_ENDED, project.isCompleted());


        if (project.getEndTime() != null)
            outState.putLong(BundleHelper.DEADLINE, project.getEndTime().getTime());
        else
            outState.putLong(BundleHelper.DEADLINE, Long.MIN_VALUE);

//        if (task.getReminder() != null) {
//
//        }

        super.onSaveInstanceState(outState);
    }

}
