package es.iespuertodelacruz.dam.gtdnow.utility;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;

public class PlaceAdapter extends BaseAdapter {
    protected Activity activity;
    protected List<Place> places;

    public PlaceAdapter (Activity activity, List<Place> places) {
        this.activity = activity;
        this.places = places;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_limitless, null);
        }

        Place place = places.get(position);

        TextView name = v.findViewById(R.id.textview_item_limitless_namevalue);
        name.setText(place.getName());

        return v;
    }
}
