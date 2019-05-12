package es.iespuertodelacruz.dam.gtdnow.utility.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.R;
import es.iespuertodelacruz.dam.gtdnow.model.entity.NamedEntity;

public class GenericAdapter<T extends NamedEntity> extends RecyclerView.Adapter<GenericAdapter.GenericViewHolder>{

    private List<T> list;
    private GenericAdapter.OnItemClickListener itemClickListener;
    private GenericAdapter.OnItemLongClickListener itemLongClickListener;

    public GenericAdapter(List<T> list, OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener) {
        this.list = list;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
    }

    @NonNull
    @Override
    public GenericAdapter.GenericViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_limitless, viewGroup, false);
        GenericAdapter.GenericViewHolder viewHolder = new GenericAdapter.GenericViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GenericAdapter.GenericViewHolder viewHolder, int i) {
        viewHolder.bind(list.get(i).getName(), this.itemClickListener, this.itemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class GenericViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;

        public GenericViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textview_item_limitless_namevalue);
        }

        public void bind(final String name, final GenericAdapter.OnItemClickListener onItemClickListener, final GenericAdapter.OnItemLongClickListener onItemLongClickListener) {
            this.nameTextView.setText(name);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(name, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.OnItemLongClick(getAdapterPosition(), v);
                }
            });
        }

    }

    public interface OnItemClickListener {
        void OnItemClick(String name, int position);
    }

    public interface OnItemLongClickListener {
        boolean OnItemLongClick(int position, View view);
    }
}
