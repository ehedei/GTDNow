package es.iespuertodelacruz.dam.gtdnow.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.TaskAdapter;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class DisplayerTaskActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RealmResults<Task> tasks;
    private String[] spinnerState;
    private Spinner spinnerGroupBy;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectors);

        //spinnerGroupBy = findViewById(R.id.spinner_selector_groupby);

        FloatingActionButton fab = findViewById(R.id.fab);

        realm = Realm.getDefaultInstance();
        tasks = getTasks();

        recyclerView = findViewById(R.id.recyclerview_selector);
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new TaskAdapter(tasks, new TaskAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String name, int position) {
                Intent intent = new Intent(getApplicationContext(), SelectorNoteActivity.class);
                intent.putExtra(BundleHelper.TASK_ID, tasks.get(position).getTaskId());
                startActivity(intent);
            }
        }, new TaskAdapter.OnSwitchListener() {
            @Override
            public void OnItemSwitch(boolean isEnded, int position) {
                Task task = tasks.get(position);
                setCompletedInRealm(task, isEnded);
            }
        }, new TaskAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick(View v, int position) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                popupMenu.inflate(R.menu.context_menu_delete_edit);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.contextmenu_delete:
                                deleteTask(tasks.get(position));
                                return true;
                            case R.id.contextmenu_edit:
                                Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
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
                Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
                startActivity(i);
            }
        });

        tasks.addChangeListener(new RealmChangeListener<RealmResults<Task>>() {
            @Override
            public void onChange(RealmResults<Task> realmResults) {
                if(!recyclerView.isComputingLayout()) {
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }


    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }


    // CRUD
    private RealmResults<Task> getTasks() {
        RealmResults<Task> tasks = null;
        Intent i = getIntent();

        if (i.getStringExtra(BundleHelper.GROUP_ID) != null) {
            tasks = realm.where(Task.class).equalTo("groups.groupId", i.getStringExtra(BundleHelper.GROUP_ID)).sort("isCompleted", Sort.ASCENDING).findAll();

        }
        else if (i.getStringExtra(BundleHelper.PROJECT_ID) != null) {
            tasks = realm.where(Task.class).equalTo("project.projectId", i.getStringExtra(BundleHelper.PROJECT_ID)).sort("isCompleted", Sort.ASCENDING).findAll();

        }
        else if (i.getStringExtra(BundleHelper.PLACE_ID) != null) {
            tasks = realm.where(Task.class).equalTo("place.placeId", i.getStringExtra(BundleHelper.PLACE_ID)).sort("isCompleted", Sort.ASCENDING).findAll();

        }
        else {
            tasks = realm.where(Task.class).sort("isCompleted", Sort.ASCENDING).findAll();
        }

        return tasks;
    }


    private void deleteTask(@NotNull Task task) {
        realm.beginTransaction();
        task.deleteFromRealm();
        realm.commitTransaction();
    }

    private void setCompletedInRealm(@NotNull Task task, boolean isEnded) {
        realm.beginTransaction();
        task.setCompleted(isEnded);
        realm.copyToRealmOrUpdate(task);
        realm.commitTransaction();
    }

}