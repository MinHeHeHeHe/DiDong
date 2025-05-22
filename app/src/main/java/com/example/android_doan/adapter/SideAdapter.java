package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.model.Side;

import java.util.ArrayList;
import java.util.List;

public class SideAdapter extends RecyclerView.Adapter<SideAdapter.SideViewHolder> {

    private List<Side> sides = new ArrayList<>();

    public SideAdapter(List<Side> sides) {
        if (sides != null) {
            this.sides = sides;
        }
    }

    @NonNull
    @Override
    public SideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new SideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SideViewHolder holder, int position) {
        Side side = sides.get(position);
        holder.txtName.setText(side.getName());
        holder.txtPrice.setText("$" + side.getBasePrice());

        if (side.getImageUrl() != null && !side.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(side.getImageUrl())
                    .into(holder.imgDish);
        } else {
            holder.imgDish.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return sides.size();
    }

    public void updateSides(List<Side> newSides) {
        if (newSides != null) {
            this.sides = newSides;
            notifyDataSetChanged();
        }
    }

    static class SideViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imgDish;

        public SideViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            imgDish = itemView.findViewById(R.id.img_dish);
        }
    }
}
