package es.iespuertodelacruz.dam.gtdnow.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Note;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity {
    private List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DisplayerTaskActivity.class);
                startActivity(i);

            }
        });
        Button b2 = findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DisplayerProjectActivity.class);
                startActivity(i);

            }
        });


        Button b3 = findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DisplayerGroupActivity.class);
                startActivity(i);

            }
        });

        Button b5 = findViewById(R.id.button5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DisplayerPlaceActivity.class);
                startActivity(i);

            }
        });
        fillDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillDB() {
        Project p1 = new Project("Hacerme programador");
        Project p2 = new Project("Arreglar vida");


        Place place1 = new Place("Casa");
        Place place2 = new Place("Oficina");
        Place place3 = new Place("Playa");

        Group group1 = new Group("Hacer en Praga");
        Group group2 = new Group("Hacer mañana");
        Group group3 = new Group("Hacer urgente");

        RealmList<Note> notes1 = new RealmList<>();
        notes1.add(new Note("Manzanas"));
        notes1.add(new Note("Pan"));
        notes1.add(new Note("Queso"));
        notes1.add(new Note("Jamón"));
        notes1.add(new Note("Ensaladas"));
        notes1.add(new Note("CocaCola Zero"));

        RealmList<Note> notes2 = new RealmList<>();
        notes2.add(new Note("Fregar suelo"));
        notes2.add(new Note("Limpiar polvo"));
        notes2.add(new Note("Cambiar sábanas"));
        notes2.add(new Note("Fregar loza"));
        notes2.add(new Note("Barrer"));


        Task task1 = new Task("Hacer la compra");
        task1.setPlace(place1);
        task1.setNotes(notes1);
        task1.setGroups(new RealmList<>());
        task1.getGroups().add(group3);
        task1.getGroups().add(group2);

        Task task2 = new Task("Programar android");
        task2.setPlace(place1);
        task2.setProject(p1);
        task2.setGroups(new RealmList<>());
        task2.getGroups().add(group1);
        task2.getGroups().add(group2);
        task2.getGroups().add(group3);

        Task task3 = new Task("Aprender asp");
        task3.setPlace(place2);
        task3.setProject(p1);
        task3.setEndTime(new Date(1557818820));

        Task task4 = new Task("Limpiar habitacion");
        task4.setPlace(place1);
        task4.setNotes(notes2);
        task4.setCompleted(true);

        Task task5 = new Task("Hacer ejercicio");
        task5.setPlace(place3);
        task5.setProject(p2);
        task5.setGroups(new RealmList<>());
        task5.getGroups().add(group2);
        task5.getGroups().add(group3);

        Task task6 = new Task("Estudiar ingles");
        task6.setPlace(place1);
        task6.setProject(p2);
        task6.setCompleted(true);

        Task task7 = new Task("Hacer turismo");
        task7.setProject(p2);
        task7.setGroups(new RealmList<>());
        task7.getGroups().add(group1);

        tasks = new ArrayList<>();

        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        tasks.add(task4);
        tasks.add(task5);
        tasks.add(task6);
        tasks.add(task7);

       //new DBFiller().execute(tasks, null, null);


    }

    private class DBFiller extends AsyncTask<List<Task>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(List<Task>... tasks) {
            Realm realm = Realm.getDefaultInstance();
            try {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(tasks[0]);
                realm.commitTransaction();

            } finally {
                realm.close();
                return true;
            }
        }

        @Override
        protected void onPostExecute (Boolean result) {
            if (result)
            Toast.makeText(getApplicationContext(), "Datos insertados", Toast.LENGTH_LONG).show();
        }
    }
}
