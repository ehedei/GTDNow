package es.iespuertodelacruz.dam.gtdnow.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import org.jetbrains.annotations.NotNull;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.ProjectDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.TaskDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import es.iespuertodelacruz.dam.gtdnow.utility.MenuListener;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.GenericDeadlineAdapter;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class DisplayerTaskFromProjectActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RealmResults<Task> tasks;
    private Realm realm;
    private TaskDao taskDao;
    private Project project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayer);

        FloatingActionButton fab = findViewById(R.id.fab);

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

        project = new ProjectDao().getProjectById(getIntent().getStringExtra(BundleHelper.PROJECT_ID));

        setTitle(project.getName() + ": " + getString(R.string.all_project));

        taskDao = new TaskDao();

        tasks = taskDao.getTasksByProject(project.getProjectId());

        recyclerView = findViewById(R.id.recyclerview_selector);
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new GenericDeadlineAdapter<>(tasks, new GenericDeadlineAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String name, int position) {
                Intent intent = new Intent(getApplicationContext(), DisplayerNoteActivity.class);
                intent.putExtra(BundleHelper.TASK_ID, tasks.get(position).getTaskId());
                startActivityForResult(intent, BundleHelper.EDIT_TASK_ACTIVITY);
            }
        }, new GenericDeadlineAdapter.OnSwitchListener() {
            @Override
            public void OnItemSwitch(boolean isEnded, int position) {
                taskDao.setCompleted(tasks.get(position), isEnded);
            }
        }, new GenericDeadlineAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick(View v, int position) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                popupMenu.inflate(R.menu.context_menu_delete_edit);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.contextmenu_delete:
                                taskDao.setProject(tasks.get(position), null);
                                return true;
                            case R.id.contextmenu_edit:
                                Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
                                i.putExtra(BundleHelper.EDIT_TASK_MODE, BundleHelper.TASK_FROM_PROJECT);
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
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SelectorTaskFromProjectActivity.class);
                i.putExtra(BundleHelper.PROJECT_ID, project.getProjectId());
                startActivity(i);
            }
        });

        tasks.addChangeListener(new RealmChangeListener<RealmResults<Task>>() {
            @Override
            public void onChange(@NotNull RealmResults<Task> realmResults) {
                if(!recyclerView.isComputingLayout()) {
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == BundleHelper.EDIT_TASK_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                if(!recyclerView.isComputingLayout()) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
