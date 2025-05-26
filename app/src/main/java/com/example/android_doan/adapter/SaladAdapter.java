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
import com.example.android_doan.model.Salad;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SaladAdapter extends RecyclerView.Adapter<SaladAdapter.SaladViewHolder> implements Filterable {

    private List<Salad> saladList;
    private List<Salad> saladListFull;

    public SaladAdapter(List<Salad> saladList) {
        this.saladList = saladList;
        this.saladListFull = new ArrayList<>(saladList);
    }

    @NonNull
    @Override
    public SaladViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pizza, parent, false);
        return new SaladViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaladViewHolder holder, int position) {
        Salad salad = saladList.get(position);
        holder.txtName.setText(salad.getName());
        holder.txtPrice.setText(String.format("%.0f₫", salad.getBasePrice()));

        Glide.with(holder.itemView.getContext())
                .load(salad.getImageUrl())
                .into(holder.imgDish);

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailItemActivity.class);
            intent.putExtra("salad", salad);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return saladList != null ? saladList.size() : 0;
    }

    public void updateSalads(List<Salad> newList) {
        saladList.clear();
        saladList.addAll(newList);
        saladListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Salad> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(saladListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Salad item : saladListFull) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
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
                saladList.clear();
                if (results.values != null) {
                    saladList.addAll((List<Salad>) results.values);
                }
                notifyDataSetChanged();
            }
        };
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
