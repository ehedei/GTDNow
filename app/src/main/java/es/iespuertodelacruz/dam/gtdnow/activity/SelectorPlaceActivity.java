package es.iespuertodelacruz.dam.gtdnow.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.PlaceDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import es.iespuertodelacruz.dam.gtdnow.utility.MenuListener;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.NamedEntitySelectorAdapter;
import io.realm.Realm;
import io.realm.RealmResults;

public class SelectorPlaceActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private RealmResults<Place> places;
    private Realm realm;
    private ListView listView;
    private PlaceDao placeDao;
    private NamedEntitySelectorAdapter<Place> adapter;

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
        placeDao = new PlaceDao();

        setTitle(getString(R.string.all_places));

        prepareActivity();
    }

    private void prepareActivity() {
        places = placeDao.getPlaces();

        adapter = new NamedEntitySelectorAdapter<>(places, getApplicationContext(), R.layout.item_limitless);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                backWithResult(places.get(position).getPlaceId());
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForEditingPlace();
            }
        });
    }

    private void backWithResult(String placeId) {
        Intent intent = getIntent();
        intent.putExtra(BundleHelper.PLACE_ID, placeId);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showAlertForEditingPlace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,  R.style.AppCompatAlertDialogStyle);

        Place place = new Place();

        String title = getResources().getString(R.string.editplace_title_create);
        String message = getResources().getString(R.string.editplace_message_create);

        builder.setTitle(title);
        builder.setMessage(message);

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_edit_simple, null);
        builder.setView(view);

        final EditText editText = view.findViewById(R.id.edittext_editgroup_name);

        builder.setPositiveButton(R.string.all_button_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString().trim();

                if (name.isEmpty())
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.message_name_required), Toast.LENGTH_SHORT).show();
                else {
                    place.setName(name);
                    placeDao.createOrUpdatePlace(place);
                    backWithResult(place.getPlaceId());
                }
            }
        });
        builder.create().show();
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