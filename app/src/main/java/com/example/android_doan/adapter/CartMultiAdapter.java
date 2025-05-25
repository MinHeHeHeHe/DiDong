package com.example.android_doan.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.UpdateCartRequest;
import com.example.android_doan.UpdateCartResponse;
import com.example.android_doan.model.Cart;
import com.example.android_doan.model.Topping;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartMultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PIZZA = 0;
    private static final int TYPE_OTHERS = 1;

    private final List<Object> items;

    public CartMultiAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof Cart.CartPizza) {
            return TYPE_PIZZA;
        }
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
        Object item = items.get(position);
        if (holder instanceof PizzaViewHolder) {
            ((PizzaViewHolder) holder).bind((Cart.CartPizza) item);
        } else {
            ((OthersViewHolder) holder).bind(item);
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


    // update cart
    private static void updateQuantity(Context context, String type, int index, int newQuantity) {
        String token = getToken(context);
        if (token == null) return;

        ApiService api = RetrofitClient.getApiService();
        UpdateCartRequest request = new UpdateCartRequest(type, index, newQuantity);

        api.updateCartItemQuantity("Bearer " + token, request).enqueue(new Callback<UpdateCartResponse>() {
            @Override
            public void onResponse(Call<UpdateCartResponse> call, Response<UpdateCartResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("CartUpdate", " " + response.body().getMessage());
                } else {
                    Log.e("CartUpdate", " Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UpdateCartResponse> call, Throwable t) {
                Log.e("CartUpdate", "Network error: " + t.getMessage());
            }
        });
    }

    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish;
        TextView txtName, txtDetail, txtPrice;

        ImageButton btnRemove;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDetail = itemView.findViewById(R.id.txt_detail);
            txtPrice = itemView.findViewById(R.id.txt_price);

        }

        public void bind(Cart.CartPizza cartPizza) {
            Context context = itemView.getContext();

            // Nếu là pizza mặc định (có pizzaId)
            if (cartPizza.getPizzaId() != null) {
                txtName.setText(cartPizza.getPizzaId().getName());
                String imageUrl = cartPizza.getPizzaId().getImageUrl();
                Log.d("PizzaImage", "Default Pizza imageUrl: " + imageUrl);

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(context).load(imageUrl).into(imgDish);
                }
            }

            // Nếu là custom pizza
            if (cartPizza.getCustomPizza() != null) {
                txtName.setText(cartPizza.getCustomPizza().getName());

                // ✅ Load ảnh nếu có pizzaId trong customPizza
                if (cartPizza.getCustomPizza().getCustomPizzaId() != null) {
                    String imageUrl = cartPizza.getCustomPizza().getImageUrl();
                    Log.d("PizzaImage", "Custom Pizza imageUrl: " + imageUrl);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(context).load(imageUrl).into(imgDish);
                    }
                } else {
                    Log.d("PizzaImage", "Custom Pizza has no pizzaId");
                }

                // Toppings
                StringBuilder toppings = new StringBuilder();
                List<Topping> toppingList = cartPizza.getCustomPizza().getToppings();
                if (toppingList != null) {
                    for (int i = 0; i < toppingList.size(); i++) {
                        toppings.append(toppingList.get(i).getName());
                        if (i < toppingList.size() - 1) toppings.append(", ");
                    }
                }

                txtDetail.setText("Topping: " + toppings + "\n Size: " +
                        cartPizza.getCustomPizza().getSize() +
                        " - Đế: " + cartPizza.getCustomPizza().getCrust_type());
                txtPrice.setText(String.format("%.0f", cartPizza.getCustomPizza().getBase_price()));
            }


        }



    }

    static class OthersViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDish;
        TextView txtName, txtDetail, txtPrice;


        public OthersViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDish = itemView.findViewById(R.id.img_dish);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDetail = itemView.findViewById(R.id.txt_detail);
            txtPrice = itemView.findViewById(R.id.txt_price);

        }

        public void bind(Object item) {
            Context context = itemView.getContext();
            String type = "";
            int quantity = 1;

            if (item instanceof Cart.CartDrink) {
                Cart.CartDrink drink = (Cart.CartDrink) item;
                txtName.setText(drink.getDrinkId().getName());
                txtPrice.setText(String.format("%.0f", drink.getDrinkId().getBasePrice()));
                Glide.with(context).load(drink.getDrinkId().getImageUrl()).into(imgDish);
                quantity = drink.getQuantity();
                type = "drink";
            } else if (item instanceof Cart.CartSide) {
                Cart.CartSide side = (Cart.CartSide) item;
                txtName.setText(side.getSideId().getName());
                txtPrice.setText(String.format("%.0f", side.getSideId().getBasePrice()));
                Glide.with(context).load(side.getSideId().getImageUrl()).into(imgDish);
                quantity = side.getQuantity();
                type = "side";
            } else if (item instanceof Cart.CartSalad) {
                Cart.CartSalad salad = (Cart.CartSalad) item;
                txtName.setText(salad.getSaladId().getName());
                txtPrice.setText(String.format("%.0f", salad.getSaladId().getBasePrice()));
                Glide.with(context).load(salad.getSaladId().getImageUrl()).into(imgDish);
                quantity = salad.getQuantity();
                type = "salad";
            }


        }


    }
}
