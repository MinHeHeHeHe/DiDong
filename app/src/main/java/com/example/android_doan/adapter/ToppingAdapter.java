package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.model.Topping;

import java.util.ArrayList;
import java.util.List;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ToppingViewHolder> {

    private List<Topping> toppingList;

    public ToppingAdapter(List<Topping> toppingList) {
        this.toppingList = toppingList;
    }

    public void updateToppings(List<Topping> newToppings) {
        this.toppingList = newToppings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topping, parent, false);
        return new ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder holder, int position) {
        Topping topping = toppingList.get(position);
        holder.bind(topping);
    }

    @Override
    public int getItemCount() {
        return toppingList != null ? toppingList.size() : 0;
    }

    public List<String> getSelectedToppingIds() {
        List<String> selectedIds = new ArrayList<>();
        for (Topping topping : toppingList) {
            if (topping.isSelected()) {
                selectedIds.add(topping.getId());
            }
        }
        return selectedIds;
    }

    public static class ToppingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTopping;
        View overlaySelected;

        public ToppingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTopping = itemView.findViewById(R.id.image_topping);
            overlaySelected = itemView.findViewById(R.id.overlay_selected);
        }

        public void bind(Topping topping) {
            Glide.with(itemView.getContext())
                    .load(topping.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageTopping);

            // Set overlay state
            overlaySelected.setVisibility(topping.isSelected() ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(v -> {
                boolean newState = !topping.isSelected();
                topping.setSelected(newState);
                overlaySelected.setVisibility(newState ? View.VISIBLE : View.GONE);
            });
        }
    }
}
