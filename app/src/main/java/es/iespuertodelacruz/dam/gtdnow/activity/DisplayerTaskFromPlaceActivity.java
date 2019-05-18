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

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.GroupDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.PlaceDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.TaskDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.GenericDeadlineAdapter;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class DisplayerTaskFromPlaceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RealmResults<Task> tasks;
    private Realm realm;
    private TaskDao taskDao;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayer);

        FloatingActionButton fab = findViewById(R.id.fab);

        realm = Realm.getDefaultInstance();

        place = new PlaceDao().getPlaceById(getIntent().getStringExtra(BundleHelper.PLACE_ID));

        setTitle(place.getName() + " - " + getString(R.string.all_tasks));

        taskDao = new TaskDao();

        tasks = taskDao.getTasksByPlace(place.getPlaceId());

        recyclerView = findViewById(R.id.recyclerview_selector);
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new GenericDeadlineAdapter<Task>(tasks, new GenericDeadlineAdapter.OnItemClickListener() {
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
                                taskDao.setPlace(tasks.get(position), null);
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
                Intent i = new Intent(getApplicationContext(), SelectorTaskFromPlaceActivity.class);
                i.putExtra(BundleHelper.PLACE_ID, place.getPlaceId());
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
}
