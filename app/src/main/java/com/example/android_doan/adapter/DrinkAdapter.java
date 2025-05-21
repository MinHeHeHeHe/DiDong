package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.model.Drink;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    private List<Drink> drinks = new ArrayList<>();

    public DrinkAdapter(List<Drink> drinks) {
        if (drinks != null) {
            this.drinks = drinks;
        }
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        Drink drink = drinks.get(position);
        holder.txtName.setText(drink.getName());
        holder.txtPrice.setText("$" + drink.getBasePrice());

        if (drink.getImageUrl() != null && !drink.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(drink.getImageUrl())
                    .into(holder.imgDish);
        } else {
            holder.imgDish.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public void updateDrinks(List<Drink> newDrinks) {
        if (newDrinks != null) {
            this.drinks = newDrinks;
            notifyDataSetChanged();
        }
    }

    static class DrinkViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imgDish;

        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            imgDish = itemView.findViewById(R.id.img_dish);
        }
    }
}
