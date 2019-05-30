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
import android.widget.PopupMenu;

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

public class SelectorTaskActivity extends MenuActivity {
    private int mode;
    private FloatingActionButton fab;
    private RealmResults<Task> tasks;
    private Realm realm;
    private ListView listView;
    private Intent intent;
    private TaskDao taskDao;
    private FinalizableEntitySelectorAdapter<Task> adapter;

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

        intent = getIntent();
        mode = intent.getIntExtra(BundleHelper.EDIT_TASK_MODE, BundleHelper.TASK_ALL);

        prepareActivity();

    }

    private void prepareActivity() {
        switch (mode) {
            case BundleHelper.TASK_FROM_GROUP:
                String groupId = intent.getStringExtra(BundleHelper.GROUP_ID);
                tasks = taskDao.getTasksByGroup(groupId);
                break;
            case BundleHelper.TASK_FROM_PROJECT:
                String projectId = intent.getStringExtra(BundleHelper.PROJECT_ID);
                tasks = taskDao.getTasksByGroup(projectId);
                break;

            case BundleHelper.TASK_FROM_PLACE:
                String placeId = intent.getStringExtra(BundleHelper.PLACE_ID);
                tasks = taskDao.getTasksByGroup(placeId);
                break;

            default:
                tasks = taskDao.getTasks();
        }

        adapter = new FinalizableEntitySelectorAdapter<>(tasks, getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra(BundleHelper.TASK_ID, tasks.get(position).getTaskId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.inflate(R.menu.context_menu_delete_edit);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.contextmenu_delete:
                                deleteFromList(tasks.get(position));
                                adapter.notifyDataSetChanged();
                                return true;
                            case R.id.contextmenu_edit:
                                Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
                                i.putExtra(BundleHelper.EDIT_TASK_MODE, mode);
                                i.putExtra(BundleHelper.TASK_ID, tasks.get(position).getTaskId());
                                startActivity(i);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
                i.putExtra(BundleHelper.EDIT_TASK_MODE, mode);

                //startActivityForResult(i);
            }
        });
    }

    private void deleteFromList(Task task) {
        switch (mode) {
            case BundleHelper.TASK_FROM_GROUP:
                String groupId = intent.getStringExtra(BundleHelper.GROUP_ID);
                Group group = new GroupDao().getGroupById(groupId);
                taskDao.removeGroup(task, group);
                break;
            case BundleHelper.TASK_FROM_PROJECT:
                taskDao.setProject(task, null);
                break;

            case BundleHelper.TASK_FROM_PLACE:
                taskDao.setPlace(task, null);
                break;

        }

    }

}
