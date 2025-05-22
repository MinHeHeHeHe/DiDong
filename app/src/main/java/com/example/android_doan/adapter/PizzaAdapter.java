package com.example.android_doan.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android_doan.Activity.DetailPizzaActivity;
import com.example.android_doan.R;
import com.example.android_doan.model.Pizza;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    private List<Pizza> pizzas;

    public PizzaAdapter(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_pizza, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = pizzas.get(position);
        holder.txtName.setText(pizza.getName());
        holder.txtPrice.setText("$" + pizza.getBasePrice());
        // Load ảnh từ URL MongoDB (image_url)
        if (pizza.getImageUrl() != null && !pizza.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(pizza.getImageUrl())

                    .into(holder.imgDish);
        } else {
            holder.imgDish.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailPizzaActivity.class);
            intent.putExtra("pizza", pizza);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pizzas != null ? pizzas.size() : 0;
    }

    public void updatePizzas(List<Pizza> newPizzas) {
        this.pizzas = newPizzas;
        notifyDataSetChanged();
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
