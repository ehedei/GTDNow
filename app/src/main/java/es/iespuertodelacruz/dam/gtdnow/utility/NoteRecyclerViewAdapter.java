package es.iespuertodelacruz.dam.gtdnow.utility;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Note;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder> {
    private List<Note> notes;
    private int layout;
    private OnItemClickListener itemClickListener;
    private OnSwitchListener switchListener;

    public NoteRecyclerViewAdapter(List<Note> notes, int layout, OnItemClickListener itemClickListener, OnSwitchListener switchListener) {
        this.notes = notes;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
        this.switchListener = switchListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        NoteViewHolder viewHolder = new NoteViewHolder(v);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(NoteViewHolder viewHolder, int i) {
        viewHolder.bind(notes.get(i).getName(), notes.get(i).isCompleted(), itemClickListener, switchListener);
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private Switch isCompletedSwitch;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textview_item_deadline_namevalue);
            isCompletedSwitch = itemView.findViewById(R.id.switch_item_deadline_ended);
        }

        public void bind(final String name, final boolean isCompleted, final OnItemClickListener onItemClickListener, final OnSwitchListener onSwitchListener) {
            this.nameTextView.setText(name);
            this.isCompletedSwitch.setChecked(isCompleted);
            this.isCompletedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onSwitchListener.OnItemSwitch(isChecked, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    return onItemClickListener.OnItemClick(name, getAdapterPosition());
                }
            });



        }
    }

    public interface OnItemClickListener {
        boolean OnItemClick(String name, int position);
    }

    public interface OnSwitchListener {
        void OnItemSwitch(boolean isEnded, int position);
    }

}
