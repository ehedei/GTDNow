package es.iespuertodelacruz.dam.gtdnow.activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.R;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;

public class SelectorPlaceActivity extends AppCompatActivity {
    private ListView listView;
    private List<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectors);


        listView = findViewById(R.id.recyclerview_selector);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Texto: " + places.get(position).getName() + "\nID: " + places.get(position).getPlaceId(), Toast.LENGTH_SHORT).show();
            }
        });




    }



}
