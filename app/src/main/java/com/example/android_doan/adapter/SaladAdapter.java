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
import com.example.android_doan.model.Salad;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SaladAdapter extends RecyclerView.Adapter<SaladAdapter.SaladViewHolder> {

    private List<Salad> salads;

    public SaladAdapter(List<Salad> salads) {
        this.salads = salads;
    }

    @NonNull
    @Override
    public SaladViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_pizza, parent, false); // Dùng chung layout
        return new SaladViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaladViewHolder holder, int position) {
        Salad salad = salads.get(position);
        holder.txtName.setText(salad.getName());
        holder.txtPrice.setText("$" + salad.getBasePrice());

        if (salad.getImageUrl() != null && !salad.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(salad.getImageUrl())
                    .into(holder.imgDish);
        } else {
            holder.imgDish.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailItemActivity.class);
            intent.putExtra("item", salad);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return salads != null ? salads.size() : 0;
    }

    public void updateSalads(List<Salad> newSalads) {
        this.salads = newSalads;
        notifyDataSetChanged();
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
