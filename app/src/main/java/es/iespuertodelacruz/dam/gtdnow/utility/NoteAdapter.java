package es.iespuertodelacruz.dam.gtdnow.utility;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Note;

public class NoteAdapter extends BaseAdapter {
    protected Activity activity;
    protected List<Note> notes;

    public NoteAdapter (Activity activity, List<Note> notes) {
        this.activity = activity;
        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // View Holder pattern
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inf = LayoutInflater.from(activity);
            convertView = inf.inflate(R.layout.item_deadline, null);

            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.textview_item_deadline_namevalue);
            holder.isCompletedSwitch = convertView.findViewById(R.id.switch_item_deadline_ended);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Note note = notes.get(position);

        holder.nameTextView.setText(note.getName());

        holder.isCompletedSwitch.setChecked(note.isCompleted());

        return convertView;
    }

    private static class ViewHolder {
        private TextView nameTextView;
        private Switch isCompletedSwitch;
    }
}
