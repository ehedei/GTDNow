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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.GroupDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.PlaceDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.ProjectDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.TaskDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.GenericDeadlineAdapter;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.SpinAdapter;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class DisplayerTaskActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private GenericDeadlineAdapter<Task> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RealmResults<Task> tasks;
    private Realm realm;
    private TaskDao taskDao;
    private Spinner spinnerCategory;
    private Spinner spinnerGroupBy;
    private FloatingActionButton fab;
    private String[] categories;
    private Group group;
    private Project project;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayer_task);

        categories = new String[] {
                getString(R.string.all_alltasks),
                getString(R.string.all_projects),
                getString(R.string.all_places),
                getString(R.string.all_groups)
        };

        spinnerCategory = findViewById(R.id.spinner_selector_category);
        spinnerGroupBy = findViewById(R.id.spinner_selector_groupby);

        fab = findViewById(R.id.fab);

        recyclerView = findViewById(R.id.recyclerview_selector);
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        realm = Realm.getDefaultInstance();

        taskDao = new TaskDao();

        prepareSpinnerCategory();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
                if (project != null) {
                    i.putExtra(BundleHelper.PROJECT_ID, project.getProjectId());
                    i.putExtra(BundleHelper.EDIT_TASK_MODE, BundleHelper.TASK_FROM_PROJECT);
                }
                else if (place != null) {
                    i.putExtra(BundleHelper.PLACE_ID, place.getPlaceId());
                    i.putExtra(BundleHelper.EDIT_TASK_MODE, BundleHelper.TASK_FROM_PLACE);
                }
                else if (group != null)
                    i.putExtra(BundleHelper.GROUP_ID, group.getGroupId());

                startActivity(i);
            }
        });

    }

    private void prepareSpinnerCategory() {
        ArrayAdapter<String> adapter = new ArrayAdapter<> (this, android.R.layout.simple_spinner_item,
                categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        fillSpinnerGroupByProject();
                        spinnerGroupBy.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        fillSpinnerGroupByPlace();
                        spinnerGroupBy.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        fillSpinnerGroupByGroup();
                        spinnerGroupBy.setVisibility(View.VISIBLE);
                        break;
                    default:
                        group = null;
                        project = null;
                        place = null;
                        spinnerGroupBy.setVisibility(View.GONE);
                        fillRecyclerView(taskDao.getTasks());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                group = null;
                project = null;
                place = null;
                spinnerGroupBy.setVisibility(View.GONE);
                fillRecyclerView(taskDao.getTasks());
            }
        });


    }

    private void fillSpinnerGroupByProject() {
        RealmResults<Project> projects = new ProjectDao().getProjects();
        SpinAdapter<Project> spinAdapter = new SpinAdapter<> (this, android.R.layout.simple_spinner_item, projects);

        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupBy.setAdapter(spinAdapter);
        spinnerGroupBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                project = projects.get(position);
                group = null;
                place = null;

                RealmResults<Task> tasks = taskDao.getTasksByProject(project.getProjectId());
                fillRecyclerView(tasks);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fillRecyclerView(taskDao.getTasks());
            }
        });

    }

    private void fillSpinnerGroupByPlace() {
        RealmResults<Place> places = new PlaceDao().getPlaces();
        SpinAdapter<Place> spinAdapter = new SpinAdapter<> (this, android.R.layout.simple_spinner_item, places);

        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupBy.setAdapter(spinAdapter);
        spinnerGroupBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                project = null;
                group = null;
                place = places.get(position);
                RealmResults<Task> tasks = taskDao.getTasksByPlace(place.getPlaceId());
                fillRecyclerView(tasks);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fillRecyclerView(taskDao.getTasks());
            }
        });
    }

    private void fillSpinnerGroupByGroup() {
        RealmResults<Group> groups = new GroupDao().getGroups();
        SpinAdapter<Group> spinAdapter = new SpinAdapter<> (this, android.R.layout.simple_spinner_item, groups);

        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupBy.setAdapter(spinAdapter);
        spinnerGroupBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                project = null;
                group = groups.get(position);
                place = null;
                RealmResults<Task> tasks = taskDao.getTasksByGroup(group.getGroupId());
                fillRecyclerView(tasks);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fillRecyclerView(taskDao.getTasks());
            }
        });
    }

    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    private void fillRecyclerView(RealmResults<Task> taskList) {

        GenericDeadlineAdapter<Task> deadlineAdapter = new GenericDeadlineAdapter<Task>(taskList, new GenericDeadlineAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String name, int position) {
                Intent intent = new Intent(getApplicationContext(), DisplayerNoteActivity.class);
                intent.putExtra(BundleHelper.TASK_ID, taskList.get(position).getTaskId());
                startActivity(intent);
            }
        }, new GenericDeadlineAdapter.OnSwitchListener() {
            @Override
            public void OnItemSwitch(boolean isEnded, int position) {
                taskDao.setCompleted(taskList.get(position), isEnded);
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
                                taskDao.deleteTask(taskList.get(position));
                                return true;
                            case R.id.contextmenu_edit:
                                Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
                                i.putExtra(BundleHelper.TASK_ID, taskList.get(position).getTaskId());
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
        recyclerView.setAdapter(deadlineAdapter);

        if(tasks != null) {
            tasks.removeAllChangeListeners();
        }
        tasks = taskList;

        if(adapter != null) {
            adapter.removeAllListeners();
        }

        adapter = deadlineAdapter;

        tasks.addChangeListener(new RealmChangeListener<RealmResults<Task>>() {
            @Override
            public void onChange(RealmResults<Task> realmResults) {
                if(!recyclerView.isComputingLayout()) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}