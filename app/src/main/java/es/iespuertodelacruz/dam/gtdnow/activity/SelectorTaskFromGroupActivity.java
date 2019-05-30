package es.iespuertodelacruz.dam.gtdnow.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.GroupDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.TaskDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import es.iespuertodelacruz.dam.gtdnow.utility.MenuListener;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.FinalizableEntitySelectorAdapter;
import io.realm.Realm;
import io.realm.RealmResults;

public class SelectorTaskFromGroupActivity extends MenuActivity {
    private FloatingActionButton fab;
    private RealmResults<Task> tasks;
    private Realm realm;
    private ListView listView;
    private Intent intent;
    private TaskDao taskDao;
    private FinalizableEntitySelectorAdapter adapter;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        listView = findViewById(R.id.listview_selector);
        fab = findViewById(R.id.fab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new MenuListener(this));
        toolbar.setNavigationIcon(R.mipmap.ic_gtd_inside_foreground);

        realm = Realm.getDefaultInstance();
        taskDao = new TaskDao();

        prepareActivity();
    }

    private void prepareActivity() {
        intent = getIntent();

        String groupId = intent.getStringExtra(BundleHelper.GROUP_ID);
        group = new GroupDao().getGroupById(groupId);

        setTitle(getString(R.string.all_tasks));

        tasks = taskDao.getTasksNotInGroup(group.getGroupId());

        adapter = new FinalizableEntitySelectorAdapter(tasks, getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                taskDao.addGroup(tasks.get(position), group);
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
                i.putExtra(BundleHelper.EDIT_TASK_MODE, BundleHelper.TASK_FROM_GROUP);
                i.putExtra(BundleHelper.GROUP_ID, group.getGroupId());
                startActivityForResult(i, BundleHelper.EDIT_TASK_ACTIVITY);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == BundleHelper.EDIT_TASK_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

}
