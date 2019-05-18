package es.iespuertodelacruz.dam.gtdnow.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.text.SimpleDateFormat;

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

        dateFormat = new SimpleDateFormat("dd.MM.yyyy '@' HH:mm:ss");

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
                    setResult(RESULT_OK, getIntent());
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

                fillTaskFields();
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
        if (project.getName() != null)
            projectName.setText(project.getName());
        else
            projectName.setText("");

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
}
