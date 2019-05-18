package es.iespuertodelacruz.dam.gtdnow.utility.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.entity.FinalizableEntity;

public class FinalizableEntitySelectorAdapter<T extends FinalizableEntity> extends BaseAdapter {
    private List<T> finalizables;
    private Context context;
    private SimpleDateFormat dateFormat;
    private Date actualDate;

    public FinalizableEntitySelectorAdapter(List<T> finalizables, Context context) {
        this.finalizables = finalizables;
        this.context = context;
        dateFormat = new SimpleDateFormat("dd.MM.yyyy '@' HH:mm:ss");
        actualDate = new Date();
    }

    @Override
    public int getCount() {
        return finalizables.size();
    }

    @Override
    public Object getItem(int position) {
        return finalizables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_finalizable_selector, null);
            vh = new ViewHolder();
            vh.textViewName = convertView.findViewById(R.id.textview_item_finalizable_namevalue);
            vh.textViewEndline = convertView.findViewById(R.id.textview_item_finalizable_endline);
            vh.textViewEndlineValue = convertView.findViewById(R.id.textview_item_finalizable_endline_value);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        FinalizableEntity finalizable = finalizables.get(position);
        Date date = finalizable.getEndTime();

        if(date != null) {
            vh.textViewEndlineValue.setText(dateFormat.format(date));
            vh.textViewEndlineValue.setVisibility(View.VISIBLE);
            vh.textViewEndline.setVisibility(View.VISIBLE);
            if(date.before(actualDate)) {
                vh.textViewEndlineValue.setTextColor(context.getResources().getColor(R.color.beware));
            }
            else {
                vh.textViewEndlineValue.setTextColor(context.getResources().getColor(R.color.primary_text));
            }

        }
        else {
            vh.textViewEndlineValue.setVisibility(View.GONE);
            vh.textViewEndline.setVisibility(View.GONE);
        }

        vh.textViewName.setText(finalizable.getName());

        return convertView;
    }

    public class ViewHolder {
        TextView textViewName;
        TextView textViewEndline;
        TextView textViewEndlineValue;
    }
}
