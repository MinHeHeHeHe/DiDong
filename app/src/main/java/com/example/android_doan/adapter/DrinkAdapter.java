package com.example.android_doan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android_doan.Activity.DetailItemActivity;
import com.example.android_doan.R;
import com.example.android_doan.model.Drink;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> implements Filterable {

    private List<Drink> drinkList;
    private List<Drink> drinkListFull;

    public DrinkAdapter(List<Drink> drinkList) {
        this.drinkList = drinkList;
        this.drinkListFull = new ArrayList<>(drinkList);
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pizza, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        Drink drink = drinkList.get(position);
        holder.txtName.setText(drink.getName());
        holder.txtPrice.setText(String.format("%.0f₫", drink.getBasePrice()));

        Glide.with(holder.itemView.getContext())
                .load(drink.getImageUrl())
                .into(holder.imgDish);

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailItemActivity.class);
            intent.putExtra("drink", drink);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return drinkList != null ? drinkList.size() : 0;
    }

    public void updateDrinks(List<Drink> newList) {
        drinkList.clear();
        drinkList.addAll(newList);
        drinkListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Drink> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(drinkListFull);
                } else {
                    String filterPattern = removeVietnameseAccent(constraint.toString().toLowerCase().trim().replaceAll("\\s+", ""));
                    for (Drink item : drinkListFull) {
                        String name = removeVietnameseAccent(item.getName()).replaceAll("\\s+", "");
                        if (name.contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                drinkList.clear();
                if (results.values != null) {
                    drinkList.addAll((List<Drink>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    private String removeVietnameseAccent(String str) {
        str = str.toLowerCase();
        str = str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str = str.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        str = str.replaceAll("[ìíịỉĩ]", "i");
        str = str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str = str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str = str.replaceAll("[ỳýỵỷỹ]", "y");
        str = str.replaceAll("đ", "d");
        return str;
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
