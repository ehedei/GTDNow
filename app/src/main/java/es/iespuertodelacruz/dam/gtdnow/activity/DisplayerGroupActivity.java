package es.iespuertodelacruz.dam.gtdnow.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.GroupDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.GenericAdapter;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class DisplayerGroupActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RealmResults<Group> groups;
    private Realm realm;
    private GroupDao groupDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayer);

        setTitle(R.string.all_groups);

        FloatingActionButton fab = findViewById(R.id.fab);

        realm = Realm.getDefaultInstance();

        groupDao = new GroupDao();

        groups = groupDao.getGroups();

        setTitle(getString(R.string.all_groups));


        recyclerView = findViewById(R.id.recyclerview_selector);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new GenericAdapter<>(groups, new GenericAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String name, int position) {
                Intent intent = new Intent(getApplicationContext(), DisplayerTaskFromGroupActivity.class);
                intent.putExtra(BundleHelper.GROUP_ID, groups.get(position).getGroupId());
                startActivity(intent);
            }
        }, new GenericAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick(int position, View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.inflate(R.menu.context_menu_delete_edit);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.contextmenu_delete:
                                groupDao.deleteGroup(groups.get(position));
                                return true;
                            case R.id.contextmenu_edit:
                                showAlertForEditingGroup(groups.get(position));
                                return true;
                            default: return false;
                        }
                    }
                });
                popupMenu.show();
                return false;
            }
        });

        groups.addChangeListener(new RealmChangeListener<RealmResults<Group>>() {
            @Override
            public void onChange(RealmResults<Group> groups) {
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForEditingGroup(null);
            }
        });

    }

    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    private void showAlertForEditingGroup(Group group) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,  R.style.AppCompatAlertDialogStyle);
        String title;
        String message;

        if (group == null) {
            title = getResources().getString(R.string.editgroup_title_create);
            message = getResources().getString(R.string.editgroup_message_create);
        }
        else {
            title = group.getName();
            message = getResources().getString(R.string.editgroup_message_edit);
        }

        builder.setTitle(title);
        builder.setMessage(message);

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_edit_simple, null);
        builder.setView(view);

        final EditText editText = view.findViewById(R.id.edittext_editgroup_name);

        if (group != null)
            editText.setText(group.getName());

        builder.setPositiveButton(R.string.all_button_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString().trim();

                if (name.isEmpty())
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.message_name_required), Toast.LENGTH_SHORT).show();
                else {
                    createOrEditGroup(name, group);
                }
            }
        });
        builder.create().show();
    }


    // CRUD
    private void createOrEditGroup(String name, Group group) {
        Group g;
        if (group == null) {
            g = new Group();
        }
        else
            g = group.clone();
        g.setName(name);
        groupDao.createOrUpdateGroup(g);
    }

}