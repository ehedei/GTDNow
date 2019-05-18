package es.iespuertodelacruz.dam.gtdnow.utility.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.entity.FinalizableEntity;

public class GenericDeadlineAdapter<T extends FinalizableEntity> extends RecyclerView.Adapter<GenericDeadlineAdapter.GenericDeadlineViewHolder> {
    private List<T> list;
    private GenericDeadlineAdapter.OnItemClickListener itemClickListener;
    private GenericDeadlineAdapter.OnSwitchListener switchListener;
    private GenericDeadlineAdapter.OnItemLongClickListener itemLongClickListener;

    public GenericDeadlineAdapter(List<T> list, GenericDeadlineAdapter.OnItemClickListener itemClickListener, GenericDeadlineAdapter.OnSwitchListener switchListener, GenericDeadlineAdapter.OnItemLongClickListener itemLongClickListener) {
        this.list = list;
        this.itemClickListener = itemClickListener;
        this.switchListener = switchListener;
        this.itemLongClickListener = itemLongClickListener;
    }

    @NonNull
    @Override
    public GenericDeadlineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false);
        return new GenericDeadlineAdapter.GenericDeadlineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericDeadlineViewHolder viewHolder, int i) {
        viewHolder.bind(list.get(i).getName(), list.get(i).isCompleted(), list.get(i).getEndTime(), itemClickListener, switchListener, itemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class GenericDeadlineViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView dateTextView;
        private TextView dateTextViewValue;
        private Switch isCompletedSwitch;
        private SimpleDateFormat dateFormat;
        private Date actualDate;

        public GenericDeadlineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textview_item_task_namevalue);
            dateTextViewValue = itemView.findViewById(R.id.textview_item_task_endline_value);
            dateTextView = itemView.findViewById(R.id.textview_item_task_endline);
            isCompletedSwitch = itemView.findViewById(R.id.switch_item_task_ended);
            dateFormat = new SimpleDateFormat("dd.MM.yyyy '@' HH:mm:ss");
            actualDate = new Date();

        }

        public void bind(final String name, final boolean isCompleted, final Date date, final GenericDeadlineAdapter.OnItemClickListener onItemClickListener, final GenericDeadlineAdapter.OnSwitchListener onSwitchListener, final GenericDeadlineAdapter.OnItemLongClickListener onItemLongClickListener) {

            if(date != null) {
                this.dateTextViewValue.setText(dateFormat.format(date));
                this.dateTextViewValue.setVisibility(View.VISIBLE);
                this.dateTextView.setVisibility(View.VISIBLE);
                if(date.before(actualDate)) {
                    this.dateTextViewValue.setTextColor(itemView.getContext().getResources().getColor(R.color.beware));
                }
                else {
                    this.dateTextViewValue.setTextColor(itemView.getContext().getResources().getColor(R.color.primary_text));
                }

            }
            else {
                this.dateTextViewValue.setVisibility(View.GONE);
                this.dateTextView.setVisibility(View.GONE);
            }


            this.nameTextView.setText(name);
            this.isCompletedSwitch.setChecked(isCompleted);
            this.isCompletedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onSwitchListener.OnItemSwitch(isChecked, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(name, getAdapterPosition());
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


    public interface OnItemClickListener {
        void OnItemClick(String name, int position);
    }

    public interface OnItemLongClickListener {
        boolean OnItemLongClick(View v, int position);
    }

    public interface OnSwitchListener {
        void OnItemSwitch(boolean isEnded, int position);
    }
}
