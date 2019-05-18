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
import android.widget.Toast;


import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.ProjectDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.GenericDeadlineAdapter;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class DisplayerProjectActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RealmResults<Project> projects;
    private Realm realm;
    private ProjectDao projectDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayer);


        FloatingActionButton fab = findViewById(R.id.fab);

        realm = Realm.getDefaultInstance();

        projectDao = new ProjectDao();

        projects = projectDao.getProjects();

        recyclerView = findViewById(R.id.recyclerview_selector);
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new GenericDeadlineAdapter<Project>(projects, new GenericDeadlineAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String name, int position) {
                Intent intent = new Intent(getApplicationContext(), DisplayerTaskFromProjectActivity.class);
                intent.putExtra(BundleHelper.PROJECT_ID, projects.get(position).getProjectId());
                startActivity(intent);
            }
        }, new GenericDeadlineAdapter.OnSwitchListener() {
            @Override
            public void OnItemSwitch(boolean isEnded, int position) {
                projectDao.setCompleted(projects.get(position), isEnded);

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
                                projectDao.deleteProject(projects.get(position));
                                return true;
                            case R.id.contextmenu_edit:
                                Intent i = new Intent(getApplicationContext(), EditProjectActivity.class);
                                i.putExtra(BundleHelper.PROJECT_ID, projects.get(position).getProjectId());
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
                Intent i = new Intent(getApplicationContext(), EditProjectActivity.class);
                startActivity(i);
            }
        });

        projects.addChangeListener(new RealmChangeListener<RealmResults<Project>>() {
            @Override
            public void onChange(RealmResults<Project> projects) {
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

}
