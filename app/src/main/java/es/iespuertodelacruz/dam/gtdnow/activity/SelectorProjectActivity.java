package es.iespuertodelacruz.dam.gtdnow.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.GroupDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.ProjectDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.TaskDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.FinalizableEntitySelectorAdapter;
import io.realm.Realm;
import io.realm.RealmResults;

public class SelectorProjectActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private RealmResults<Project> projects;
    private Realm realm;
    private ListView listView;
    private ProjectDao projectDao;
    private FinalizableEntitySelectorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        listView = findViewById(R.id.listview_selector);
        fab = findViewById(R.id.fab);

        setTitle(getString(R.string.all_projects));

        realm = Realm.getDefaultInstance();
        projectDao = new ProjectDao();

        prepareActivity();
    }

    private void prepareActivity() {
        projects = projectDao.getProjects();

        adapter = new FinalizableEntitySelectorAdapter(projects, getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                backWithResult(projects.get(position).getProjectId());
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditProjectActivity.class);
                startActivityForResult(i, BundleHelper.EDIT_PROJECT_ACTIVITY);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == BundleHelper.EDIT_PROJECT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String projectId = intent.getStringExtra(BundleHelper.PROJECT_ID);
                backWithResult(projectId);
            }
        }
    }

    private void backWithResult(String projectId) {
        Intent intent = getIntent();
        intent.putExtra(BundleHelper.PROJECT_ID, projectId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
