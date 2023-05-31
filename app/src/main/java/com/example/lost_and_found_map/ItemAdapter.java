package com.example.lost_and_found_map;

import java.util.List;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import androidx.recyclerview.widget.RecyclerView;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<LostFoundItem> listofItems;
    private OnItemClickListener itemListener;
    private LostFoundDAO DAOitem;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onRemoveButtonClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemListener = listener;
    }

    public ItemAdapter(List<LostFoundItem> itemList, LostFoundDAO itemDAO) {
        this.listofItems = itemList;
        this.DAOitem = itemDAO;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView TVitemName;
        Button buttonRemove;
        TextView TVdescription;
        TextView TVpostType;
        TextView TVlocation;

        public ItemViewHolder(View itemView) {
            super(itemView);
            TVitemName = itemView.findViewById(R.id.textViewItemName);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
            TVdescription = itemView.findViewById(R.id.textViewItemDescription);
            TVpostType = itemView.findViewById(R.id.textViewPostType_);
            TVlocation = itemView.findViewById(R.id.textViewLocation_);

            // Listens to the items clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemListener.onItemClick(position);
                        }
                    }
                }
            });

            // Remove button is pressed and then item is deleted
            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemListener.onRemoveButtonClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        LostFoundItem item = listofItems.get(position);

        holder.TVitemName.setText(item.getName());
        holder.TVdescription.setText(item.getDescription());

        if (item.getPostType().equals("FOUND") || item.getPostType().equals("Found")) {
            holder.TVpostType.setText("Found");
            holder.TVlocation.setText("Location: " + item.getLocation());
        } else {
            holder.TVpostType.setText("Lost");
            holder.TVlocation.setVisibility(View.GONE);
        }



        holder.buttonRemove.setOnClickListener(v -> {
            if (itemListener != null) {
                itemListener.onRemoveButtonClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listofItems.size();
    }
}

