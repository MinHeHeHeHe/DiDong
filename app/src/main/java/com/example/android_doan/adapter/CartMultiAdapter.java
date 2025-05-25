package com.example.android_doan.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.UpdateCartRequest;
import com.example.android_doan.UpdateCartResponse;
import com.example.android_doan.model.Cart;
import com.example.android_doan.model.CartDisplayItem;
import com.example.android_doan.model.Topping;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartMultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PIZZA = 0;
    private static final int TYPE_OTHERS = 1;

    private final Context context;
    private final List<CartDisplayItem> items;

    public CartMultiAdapter(Context context, List<CartDisplayItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position).getItem();
        if (item instanceof Cart.CartPizza) return TYPE_PIZZA;
        return TYPE_OTHERS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_PIZZA) {
            View view = inflater.inflate(R.layout.item_cart_pizza, parent, false);
            return new PizzaViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_cart, parent, false);
            return new OthersViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartDisplayItem cartItem = items.get(position);
        Object item = cartItem.getItem();

        if (holder instanceof PizzaViewHolder && item instanceof Cart.CartPizza) {
            ((PizzaViewHolder) holder).bind((Cart.CartPizza) item, cartItem.getMongoIndex(), cartItem.getType());
        } else {
            ((OthersViewHolder) holder).bind(cartItem);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return prefs.getString("token", null);
    }

    private void deleteItem(String type, int mongoIndex, int adapterIndex) {
        String token = getToken(context);
        if (token == null) return;

        Log.d("DELETE_ITEM", "Sending itemType=" + type + ", index=" + mongoIndex);

        ApiService api = RetrofitClient.getApiService();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("itemType", type);
        body.put("itemIndex", mongoIndex);

        api.deleteCartItem("Bearer " + token, body).enqueue(new Callback<UpdateCartResponse>() {
            @Override
            public void onResponse(Call<UpdateCartResponse> call, Response<UpdateCartResponse> response) {
                Log.d("DELETE_ITEM", "Response code: " + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                    items.remove(adapterIndex);
                    notifyItemRemoved(adapterIndex);
                    notifyItemRangeChanged(adapterIndex, items.size());
                } else {
                    Toast.makeText(context, "Lỗi xóa item: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateCartResponse> call, Throwable t) {
                Toast.makeText(context, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class PizzaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish, btnRemove;
        TextView txtName, txtDetail, txtPrice;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDetail = itemView.findViewById(R.id.txt_detail);
            txtPrice = itemView.findViewById(R.id.txt_price);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

        public void bind(Cart.CartPizza pizza, int mongoIndex, CartDisplayItem.Type type) {
            if (pizza.getPizzaId() != null) {
                txtName.setText(pizza.getPizzaId().getName());
                Glide.with(context).load(pizza.getPizzaId().getImageUrl()).into(imgDish);
                txtDetail.setText("");
                txtPrice.setText(String.format("%.0f", pizza.getPizzaId().getBasePrice()));
            } else if (pizza.getCustomPizza() != null) {
                txtName.setText(pizza.getCustomPizza().getName());
                Glide.with(context).load(pizza.getCustomPizza().getImageUrl()).into(imgDish);

                StringBuilder toppings = new StringBuilder();
                List<Topping> toppingList = pizza.getCustomPizza().getToppings();
                for (int i = 0; i < toppingList.size(); i++) {
                    toppings.append(toppingList.get(i).getName());
                    if (i < toppingList.size() - 1) toppings.append(", ");
                }

                txtDetail.setText("Topping: " + toppings + "\nSize: " +
                        pizza.getCustomPizza().getSize() +
                        " - Đế: " + pizza.getCustomPizza().getCrust_type());
                txtPrice.setText(String.format("%.0f", pizza.getCustomPizza().getBase_price()));
            }

            btnRemove.setOnClickListener(v -> {
                int adapterIndex = getAdapterPosition();
                if (adapterIndex != RecyclerView.NO_POSITION) {
                    deleteItem(type.name().toLowerCase(), mongoIndex, adapterIndex);
                }
                Log.d("CART_REMOVE", " Clicked remove button at position " + getAdapterPosition());
            });
        }
    }

    class OthersViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish, btnRemove;
        TextView txtName, txtPrice, txtDetail;

        public OthersViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            txtName = itemView.findViewById(R.id.txt_name);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtDetail = itemView.findViewById(R.id.txt_detail);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

        public void bind(CartDisplayItem cartItem) {
            Object item = cartItem.getItem();
            String type = cartItem.getType().name().toLowerCase();
            int mongoIndex = cartItem.getMongoIndex();

            if (item instanceof Cart.CartDrink) {
                Cart.CartDrink drink = (Cart.CartDrink) item;
                txtName.setText(drink.getDrinkId().getName());
                txtPrice.setText(String.format("%.0f", drink.getDrinkId().getBasePrice()));
                Glide.with(context).load(drink.getDrinkId().getImageUrl()).into(imgDish);
            } else if (item instanceof Cart.CartSide) {
                Cart.CartSide side = (Cart.CartSide) item;
                txtName.setText(side.getSideId().getName());
                txtPrice.setText(String.format("%.0f", side.getSideId().getBasePrice()));
                Glide.with(context).load(side.getSideId().getImageUrl()).into(imgDish);
            } else if (item instanceof Cart.CartSalad) {
                Cart.CartSalad salad = (Cart.CartSalad) item;
                txtName.setText(salad.getSaladId().getName());
                txtPrice.setText(String.format("%.0f", salad.getSaladId().getBasePrice()));
                Glide.with(context).load(salad.getSaladId().getImageUrl()).into(imgDish);
            }

            btnRemove.setOnClickListener(v -> {
                int adapterIndex = getAdapterPosition();
                if (adapterIndex != RecyclerView.NO_POSITION) {
                    deleteItem(type, mongoIndex, adapterIndex);
                }
            });
        }
    }
}
