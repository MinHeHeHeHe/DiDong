package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.model.SideDish;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SideDishAdapter extends RecyclerView.Adapter<SideDishAdapter.SideDishViewHolder> {

    private List<SideDish> sideDishes = new ArrayList<>();

    public SideDishAdapter(List<SideDish> sideDishes) {
        if (sideDishes != null) {
            this.sideDishes = sideDishes;
        }
    }

    @NonNull
    @Override
    public SideDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new SideDishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SideDishViewHolder holder, int position) {
        SideDish item = sideDishes.get(position);
        holder.txtName.setText(item.getName());
        holder.txtPrice.setText("$" + item.getBasePrice());

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .into(holder.imgDish);
        } else {
            holder.imgDish.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return sideDishes.size();
    }

    public void updateSideDishes(List<SideDish> newList) {
        if (newList != null) {
            this.sideDishes = newList;
            notifyDataSetChanged();
        }
    }

    static class SideDishViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imgDish;

        public SideDishViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            imgDish = itemView.findViewById(R.id.img_dish);
        }
    }
}
