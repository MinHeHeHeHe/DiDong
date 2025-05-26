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
import com.example.android_doan.model.SideDish;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SideDishAdapter extends RecyclerView.Adapter<SideDishAdapter.SideDishViewHolder> implements Filterable {

    private List<SideDish> sideDishList;
    private List<SideDish> sideDishListFull;

    public SideDishAdapter(List<SideDish> sideDishList) {
        this.sideDishList = sideDishList;
        this.sideDishListFull = new ArrayList<>(sideDishList);
    }

    @NonNull
    @Override
    public SideDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pizza, parent, false);
        return new SideDishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SideDishViewHolder holder, int position) {
        SideDish sideDish = sideDishList.get(position);
        holder.txtName.setText(sideDish.getName());
        holder.txtPrice.setText(String.format("%.0f₫", sideDish.getBasePrice()));

        Glide.with(holder.itemView.getContext())
                .load(sideDish.getImageUrl())
                .into(holder.imgDish);

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailItemActivity.class);
            intent.putExtra("side", sideDish);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sideDishList != null ? sideDishList.size() : 0;
    }

    public void updateSideDishes(List<SideDish> newList) {
        sideDishList.clear();
        sideDishList.addAll(newList);
        sideDishListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<SideDish> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(sideDishListFull);
                } else {
                    String filterPattern = removeVietnameseAccent(constraint.toString().toLowerCase().trim().replaceAll("\\s+", ""));
                    for (SideDish item : sideDishListFull) {
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
                sideDishList.clear();
                if (results.values != null) {
                    sideDishList.addAll((List<SideDish>) results.values);
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

    static class SideDishViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imgDish;

        public SideDishViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            imgDish = itemView.findViewById(R.id.img_dish);
        }
    }
}
