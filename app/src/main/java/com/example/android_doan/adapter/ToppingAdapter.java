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
import com.example.android_doan.model.Topping;

import java.util.List;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ToppingViewHolder> {

    private List<Topping> toppings;

    public ToppingAdapter(List<Topping> toppings) {
        this.toppings = toppings;
    }

    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topping, parent, false);
        return new ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder holder, int position) {
        Topping topping = toppings.get(position);
        Glide.with(holder.itemView.getContext())
                .load(topping.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground) // hiển thị ảnh mặc định nếu load thất bại
                .into(holder.image);
    }
    @Override
    public int getItemCount() {
        return toppings != null ? toppings.size() : 0;
    }

    static class ToppingViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ToppingViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_topping);
        }
    }

    public void updateToppings(List<Topping> toppings) {
        this.toppings = toppings;
        notifyDataSetChanged();
    }
}

