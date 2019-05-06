package es.iespuertodelacruz.dam.gtdnow;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.model.dao.PlaceDao;
import es.iespuertodelacruz.dam.gtdnow.model.database.GTDNowDb;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.utility.PlaceAdapter;

public class SelectorPlaceActivity extends AppCompatActivity {
    private ListView listView;
    private PlaceDao placeDao;
    private List<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectors);
        placeDao = GTDNowDb.getDb(getApplicationContext()).placeDao();

        listView = findViewById(R.id.recyclerview_selector);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Texto: " + places.get(position).getName() + "\nID: " + places.get(position).getPlaceId(), Toast.LENGTH_SHORT).show();
            }
        });


        new LoadPlacesFromDb().execute(placeDao);

    }


    private void fillListView(List<Place> places) {
        PlaceAdapter adapter = new PlaceAdapter(this, places);
        listView.setAdapter(adapter);
    }

    private class LoadPlacesFromDb extends AsyncTask<PlaceDao, Integer, List<Place>> {
        @Override
        protected List<Place> doInBackground(PlaceDao... placeDaos) {
            places = placeDaos[0].getPlaces();
            return places;
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            fillListView(places);
        }
    }

}
