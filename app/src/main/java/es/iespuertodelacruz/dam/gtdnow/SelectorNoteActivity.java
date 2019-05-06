package es.iespuertodelacruz.dam.gtdnow;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import es.iespuertodelacruz.dam.gtdnow.model.dao.NoteDao;
import es.iespuertodelacruz.dam.gtdnow.model.database.GTDNowDb;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Note;
import es.iespuertodelacruz.dam.gtdnow.utility.NoteRecyclerViewAdapter;

public class SelectorNoteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private NoteDao noteDao;
    private List<Note> notes;
    private String[] state;
    private Spinner spinner;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectors);
        findViewById(R.id.spinner_selector_category).setVisibility(View.GONE);

        notes = new ArrayList<>();

        noteDao = GTDNowDb.getDb(getApplicationContext()).noteDao();

        recyclerView = findViewById(R.id.recyclerview_selector);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new NoteRecyclerViewAdapter(notes, R.layout.item_deadline, new NoteRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public boolean OnItemClick(String name, int position) {
                Toast.makeText(getApplicationContext(), "Podría usted querer eliminar el sistema atmosferico " + name, Toast.LENGTH_SHORT).show();
                return true;
            }
        }, new NoteRecyclerViewAdapter.OnSwitchListener() {
            @Override
            public void OnItemSwitch(boolean isEnded, int position) {
                Toast.makeText(getApplicationContext(), "Acabas de cambiar el el tema de posicion " + recyclerView.findViewHolderForLayoutPosition(position) + " a " + isEnded, Toast.LENGTH_SHORT).show();
                notes.get(position).setName("Absurdo");
            }
        });
        recyclerView.setAdapter(adapter);

        prepareSpinner();

    }


    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    private void prepareSpinner() {
        spinner = findViewById(R.id.spinner_selector_groupby);

        state = new String[]{getResources().getString(R.string.notes_all),
                getResources().getString(R.string.notes_notended),
                getResources().getString(R.string.notes_ended)};

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, state);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new LoadNotesFromDb().execute(noteDao);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                new LoadNotesFromDb().execute(noteDao);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private class LoadNotesFromDb extends AsyncTask<NoteDao, Integer, List<Note>> {
        @Override
        protected List<Note> doInBackground(NoteDao... noteDaos) {
            int i = spinner.getSelectedItemPosition();

            switch (i) {
                case 1:
                    notes = noteDaos[0].getNotes(false);
                    break;
                case 2:
                    notes = noteDaos[0].getNotes(true);
                    break;
                default:
                    notes = noteDaos[0].getNotes();
            }
            return notes;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            setNotes(notes);
        }
    }

}
