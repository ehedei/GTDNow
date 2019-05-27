package es.iespuertodelacruz.dam.gtdnow.utility.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import es.iespuertodelacruz.dam.gtdnow.model.entity.NamedEntity;
import io.realm.RealmResults;

public class SpinAdapter<T extends NamedEntity> extends ArrayAdapter<T> {

    private Context context;
    private RealmResults<T> list;

    public SpinAdapter(Context context, int textViewResourceId,
                       RealmResults<T> list) {
        super(context, textViewResourceId, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public T getItem(int position){
        return list.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    @NotNull
    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);

        label.setText(list.get(position).getName());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(list.get(position).getName());

        return label;
    }
}