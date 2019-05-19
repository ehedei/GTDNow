package es.iespuertodelacruz.dam.gtdnow.utility.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.entity.NamedEntity;

public class NamedEntitySelectorAdapter<T extends NamedEntity> extends BaseAdapter {
    private List<T> entities;
    private Context context;


    public NamedEntitySelectorAdapter(List<T> entities, Context context) {
        this.entities = entities;
        this.context = context;
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NamedEntitySelectorAdapter.ViewHolder vh;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_limitless, null);
            vh = new NamedEntitySelectorAdapter.ViewHolder();
            vh.textViewName = convertView.findViewById(R.id.textview_item_limitless_namevalue);
            convertView.setTag(vh);
        } else {
            vh = (NamedEntitySelectorAdapter.ViewHolder) convertView.getTag();
        }


        NamedEntity entity = entities.get(position);

        vh.textViewName.setText(entity.getName());

        return convertView;
    }

    public class ViewHolder {
        TextView textViewName;
    }
}
