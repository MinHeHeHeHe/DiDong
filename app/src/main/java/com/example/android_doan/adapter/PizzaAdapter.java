package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.android_doan.model.Pizza;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {
    private List<Pizza> pizzas;

    public PizzaAdapter(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = pizzas.get(position);
        String displayText = String.format("Name: %s\nDescription: %s\nSize: %s\nCrust: %s\nToppings: %s\nPrice: $%.2f",
                pizza.getName(), pizza.getDescription(), pizza.getSize(), pizza.getCrustType(),
                String.join(", ", pizza.getToppings()), pizza.getBasePrice());
        holder.textView.setText(displayText);
    }

    @Override
    public int getItemCount() {
        return pizzas.size();
    }

    public void updatePizzas(List<Pizza> newPizzas) {
        this.pizzas = newPizzas;
        notifyDataSetChanged();
    }

    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        PizzaViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}