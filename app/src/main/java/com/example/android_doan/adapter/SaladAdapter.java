package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.model.Salad;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SaladAdapter extends RecyclerView.Adapter<SaladAdapter.SaladViewHolder> {

    private List<Salad> salads = new ArrayList<>();

    public SaladAdapter(List<Salad> salads) {
        if (salads != null) {
            this.salads = salads;
        }
    }

    @NonNull
    @Override
    public SaladViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_pizza, parent, false);
        return new SaladViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaladViewHolder holder, int position) {
        Salad item = salads.get(position);
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
        return salads.size();
    }

    public void updateSalads(List<Salad> newList) {
        if (newList != null) {
            this.salads = newList;
            notifyDataSetChanged();
        }
    }

    static class SaladViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imgDish;

        public SaladViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            imgDish = itemView.findViewById(R.id.img_dish);
        }
    }
}
