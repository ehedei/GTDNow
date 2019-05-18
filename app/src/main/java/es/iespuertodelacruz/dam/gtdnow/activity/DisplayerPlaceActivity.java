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
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.utility.BundleHelper;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayer);
        fab = findViewById(R.id.fab);

        realm = Realm.getDefaultInstance();
        places = realm.where(Place.class).sort("name", Sort.ASCENDING).findAll();

        recyclerView = findViewById(R.id.recyclerview_selector);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new GenericAdapter<Place>(places, new GenericAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String name, int position) {
                returnResult(places.get(position));
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
                                deletePlace(places.get(position));
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
                    createOrEditPlace(name, place);
                }
            }
        });
        builder.create().show();
    }


    // Return to activity with result
    private void returnResult(@NotNull Place place) {
        Intent intent = getIntent();
        intent.putExtra(BundleHelper.PLACE_ID, place.getPlaceId());
        setResult(RESULT_OK, intent);
        finish();
    }

    // CRUD
    private void createOrEditPlace(String name, Place place) {
        realm.beginTransaction();
        if (place == null) {
            place = new Place();
        }
        place.setName(name);
        realm.copyToRealmOrUpdate(place);
        realm.commitTransaction();
    }

    private void deletePlace(@NotNull Place place) {
        realm.beginTransaction();
        place.deleteFromRealm();
        realm.commitTransaction();
    }

}
