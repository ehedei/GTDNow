package es.iespuertodelacruz.dam.gtdnow.utility.adapter;

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

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes;
    private NoteAdapter.OnSwitchListener switchListener;
    private NoteAdapter.OnItemLongClickListener itemLongClickListener;

    public NoteAdapter(List<Note> notes, OnSwitchListener switchListener, OnItemLongClickListener itemLongClickListener) {
        this.notes = notes;
        this.switchListener = switchListener;
        this.itemLongClickListener = itemLongClickListener;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_deadline, viewGroup, false);
        NoteAdapter.NoteViewHolder viewHolder = new NoteAdapter.NoteViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder viewHolder, int i) {
        viewHolder.bind(notes.get(i).getName(), notes.get(i).isCompleted(), switchListener, itemLongClickListener);
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

        public void bind(final String name, final boolean isCompleted, final NoteAdapter.OnSwitchListener onSwitchListener, final NoteAdapter.OnItemLongClickListener onItemLongClickListener) {

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
                    return onItemLongClickListener.OnItemLongClick(v, getAdapterPosition());
                }
            });
        }

    }

    public interface OnItemLongClickListener {
        boolean OnItemLongClick(View v, int position);
    }

    public interface OnSwitchListener {
        void OnItemSwitch(boolean isEnded, int position);
    }
}
