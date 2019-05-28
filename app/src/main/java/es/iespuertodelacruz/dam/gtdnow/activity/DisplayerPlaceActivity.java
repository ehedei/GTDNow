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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.dao.PlaceDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
import es.iespuertodelacruz.dam.gtdnow.utility.MenuListener;
import es.iespuertodelacruz.dam.gtdnow.utility.adapter.GenericAdapter;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class DisplayerPlaceActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RealmResults<Place> places;
    private Realm realm;
    private PlaceDao placeDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayer);
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

        places = placeDao.getPlaces();

        setTitle(getString(R.string.all_places));

        recyclerView = findViewById(R.id.recyclerview_selector);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new GenericAdapter<>(places, new GenericAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String name, int position) {
                Intent intent = new Intent(getApplicationContext(), DisplayerTaskFromPlaceActivity.class);
                intent.putExtra(BundleHelper.PLACE_ID, places.get(position).getPlaceId());
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
                                placeDao.deletePlace(places.get(position));
                                return true;
                            case R.id.contextmenu_edit:
                                showAlertForEditingPlace(places.get(position));
                                return true;
                            default: return false;
                        }
                    }
                });
                popupMenu.show();
                return false;
            }
        });

        places.addChangeListener(new RealmChangeListener<RealmResults<Place>>() {
            @Override
            public void onChange(RealmResults<Place> places) {
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForEditingPlace(null);
            }
        });

    }

    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    private void showAlertForEditingPlace(Place place) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,  R.style.AppCompatAlertDialogStyle);
        String title;
        String message;

        if (place == null) {
            title = getResources().getString(R.string.editplace_title_create);
            message = getResources().getString(R.string.editplace_message_create);
        }
        else {
            title = place.getName();
            message = getResources().getString(R.string.editplace_message_edit);
        }

        builder.setTitle(title);
        builder.setMessage(message);

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_edit_simple, null);
        builder.setView(view);

        final EditText editText = view.findViewById(R.id.edittext_editgroup_name);

        if (place != null)
            editText.setText(place.getName());

        builder.setPositiveButton(R.string.all_button_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString().trim();

                if (name.isEmpty())
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.message_name_required), Toast.LENGTH_SHORT).show();
                else {
                    placeDao.createOrEditPlace(name, place);
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
