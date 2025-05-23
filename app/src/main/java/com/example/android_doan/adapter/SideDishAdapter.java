package com.example.android_doan.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android_doan.Activity.DetailItemActivity;
import com.example.android_doan.R;
import com.example.android_doan.model.SideDish;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SideDishAdapter extends RecyclerView.Adapter<SideDishAdapter.SideDishViewHolder> {

    private List<SideDish> sideDishes;

    public SideDishAdapter(List<SideDish> sideDishes) {
        this.sideDishes = sideDishes;
    }

    @NonNull
    @Override
    public SideDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_pizza, parent, false); // Dùng chung layout
        return new SideDishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SideDishViewHolder holder, int position) {
        SideDish sideDish = sideDishes.get(position);
        holder.txtName.setText(sideDish.getName());
        holder.txtPrice.setText("$" + sideDish.getBasePrice());

        if (sideDish.getImageUrl() != null && !sideDish.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(sideDish.getImageUrl())
                    .into(holder.imgDish);
        } else {
            holder.imgDish.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailItemActivity.class);
            intent.putExtra("item", sideDish);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sideDishes != null ? sideDishes.size() : 0;
    }

    public void updateSideDishes(List<SideDish> newSideDishes) {
        this.sideDishes = newSideDishes;
        notifyDataSetChanged();
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