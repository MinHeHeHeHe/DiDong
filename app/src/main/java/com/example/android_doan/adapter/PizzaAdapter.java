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
import com.example.android_doan.Activity.DetailPizzaActivity;
import com.example.android_doan.R;
import com.example.android_doan.model.Pizza;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> implements Filterable {

    private List<Pizza> pizzaList;
    private List<Pizza> pizzaListFull;

    public PizzaAdapter(List<Pizza> pizzaList) {
        this.pizzaList = pizzaList;
        this.pizzaListFull = new ArrayList<>(pizzaList);
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pizza, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = pizzaList.get(position);
        holder.txtName.setText(pizza.getName());
        holder.txtPrice.setText(String.format("%.0f₫", pizza.getBasePrice()));

        Glide.with(holder.itemView.getContext())
                .load(pizza.getImageUrl())
                .into(holder.imgDish);

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailPizzaActivity.class);
            intent.putExtra("pizza", pizza);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pizzaList != null ? pizzaList.size() : 0;
    }

    public void updatePizzas(List<Pizza> newList) {
        pizzaList.clear();
        pizzaList.addAll(newList);
        pizzaListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Pizza> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(pizzaListFull);
                } else {
                    String filterPattern = removeVietnameseAccent(constraint.toString().toLowerCase().trim().replaceAll("\\s+", ""));
                    for (Pizza item : pizzaListFull) {
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
                pizzaList.clear();
                if (results.values != null) {
                    pizzaList.addAll((List<Pizza>) results.values);
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

    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imgDish;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            imgDish = itemView.findViewById(R.id.img_dish);
        }
    }
}
