package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.model.Delivery;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDeliveryMultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PIZZA = 0;
    private static final int TYPE_OTHERS = 1;

    private final List<Object> items;

    public ItemDeliveryMultiAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        return (item instanceof Delivery.CartPizza) ? TYPE_PIZZA : TYPE_OTHERS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_PIZZA) {
            View view = inflater.inflate(R.layout.item_delivered_pizza, parent, false);
            return new PizzaViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_delivered_other, parent, false);
            return new OthersViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof PizzaViewHolder && item instanceof Delivery.CartPizza) {
            ((PizzaViewHolder) holder).bind((Delivery.CartPizza) item);
        } else if (holder instanceof OthersViewHolder) {
            ((OthersViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish;
        TextView txtName, txtDetail, txtPrice;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDetail = itemView.findViewById(R.id.txt_detail);
            txtPrice = itemView.findViewById(R.id.txt_price);
        }

        public void bind(Delivery.CartPizza pizza) {
            if (pizza.getPizza() != null) {
                txtName.setText(pizza.getPizza().getName());
                Glide.with(itemView.getContext()).load(pizza.getPizza().getImageUrl()).into(imgDish);
                txtDetail.setText("");
                txtPrice.setText(String.format("%,.0fđ", pizza.getPizza().getBasePrice()));
            } else if (pizza.getCustomPizza() != null) {
                Delivery.CustomPizza custom = pizza.getCustomPizza();
                txtName.setText(custom.getName());
                Glide.with(itemView.getContext()).load(custom.getImageUrl()).into(imgDish);

                String toppingText = (custom.getToppings() != null && !custom.getToppings().isEmpty())
                        ? String.join(", ", custom.getToppings())
                        : "Không có topping";

                txtDetail.setText("Topping: " + toppingText +
                        "\nSize: " + custom.getSize() + " - Đế: " + custom.getCrustType());

                txtPrice.setText(String.format("%,.0fđ", custom.getBasePrice()));
            }
        }
    }

    static class OthersViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish;
        TextView txtName, txtPrice;

        public OthersViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
        }

        public void bind(Object item) {
            if (item instanceof Delivery.CartDrink) {
                Delivery.CartDrink drink = (Delivery.CartDrink) item;
                txtName.setText(drink.getDrinkId().getName());
                txtPrice.setText(String.format("%,.0fđ", drink.getDrinkId().getBasePrice()));
                Glide.with(itemView.getContext()).load(drink.getDrinkId().getImageUrl()).into(imgDish);
            } else if (item instanceof Delivery.CartSide) {
                Delivery.CartSide side = (Delivery.CartSide) item;
                txtName.setText(side.getSideId().getName());
                txtPrice.setText(String.format("%,.0fđ", side.getSideId().getBasePrice()));
                Glide.with(itemView.getContext()).load(side.getSideId().getImageUrl()).into(imgDish);
            } else if (item instanceof Delivery.CartSalad) {
                Delivery.CartSalad salad = (Delivery.CartSalad) item;
                txtName.setText(salad.getSaladId().getName());
                txtPrice.setText(String.format("%,.0fđ", salad.getSaladId().getBasePrice()));
                Glide.with(itemView.getContext()).load(salad.getSaladId().getImageUrl()).into(imgDish);
            }
        }
    }
}
