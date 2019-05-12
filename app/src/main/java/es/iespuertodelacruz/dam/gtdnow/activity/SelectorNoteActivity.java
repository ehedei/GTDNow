package es.iespuertodelacruz.dam.gtdnow.activity;

import android.content.DialogInterface;
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
import es.iespuertodelacruz.dam.gtdnow.model.entity.Note;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.NoteAdapter;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;


public class SelectorNoteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Realm realm;
    private RealmResults<Note> notes;
    private Task task;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectors);

        FloatingActionButton fab = findViewById(R.id.fab);

        realm = Realm.getDefaultInstance();

        String taskId = getIntent().getStringExtra(BundleHelper.TASK_ID);

        task = realm.where(Task.class).equalTo("taskId", taskId).findFirst();

        notes = getNotesByTask(taskId);

        recyclerView = findViewById(R.id.recyclerview_selector);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new NoteAdapter(notes, new NoteAdapter.OnSwitchListener() {
            @Override
            public void OnItemSwitch(boolean isEnded, int position) {
                Note note = notes.get(position);
                setCompletedInRealm(note, isEnded);
            }
        }, new NoteAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick(View v, int position) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                popupMenu.inflate(R.menu.context_menu_delete_edit);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.contextmenu_delete:
                                deleteNote(notes.get(position));
                                return true;
                            case R.id.contextmenu_edit:
                                showAlertForEditingNote(notes.get(position));
                                return true;
                            default: return false;
                        }
                    }
                });
                popupMenu.show();
                return false;
            }
        });
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> showAlertForEditingNote(null));

        notes.addChangeListener(notes -> {
            if(!recyclerView.isComputingLayout()) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }


    private void showAlertForEditingNote(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,  R.style.AppCompatAlertDialogStyle);
        String title;
        String message;

        if (note == null) {
            title = getResources().getString(R.string.editnote_title_create);
            message = getResources().getString(R.string.editnote_message_create);
        }
        else {
            title = note.getName();
            message = getResources().getString(R.string.editnote_message_edit);
        }

        builder.setTitle(title);
        builder.setMessage(message);

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_edit_simple, null);
        builder.setView(view);

        final EditText editText = view.findViewById(R.id.edittext_editgroup_name);

        if (note != null)
            editText.setText(note.getName());

        builder.setPositiveButton(R.string.all_button_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString().trim();

                if (name.isEmpty())
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.message_name_required), Toast.LENGTH_SHORT).show();
                else {
                    createOrEditNote(name, note);
                }
            }
        });
        builder.create().show();
    }



    // CRUD
    private RealmResults<Note> getNotesByTask(String taskId) {
        return realm.where(Note.class).equalTo("task.taskId", taskId).sort("isCompleted", Sort.ASCENDING).findAll();
    }

    private void deleteNote(Note note) {
        realm.beginTransaction();
        note.deleteFromRealm();
        realm.commitTransaction();
    }

    private void createOrEditNote(String name, Note note) {
        realm.beginTransaction();
        if (note == null) {
            note = new Note();
            task.getNotes().add(note);
        }
        note.setName(name);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();

    }

    private void setCompletedInRealm(@NotNull Note note, boolean isEnded) {
        realm.beginTransaction();
        note.setCompleted(isEnded);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
    }


}
