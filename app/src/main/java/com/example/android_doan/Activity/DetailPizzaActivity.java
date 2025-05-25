package com.example.android_doan.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.AddToCartRequest;
import com.example.android_doan.R;
import com.example.android_doan.adapter.ToppingAdapter;
import com.example.android_doan.model.Pizza;
import com.example.android_doan.model.Topping;
import com.example.android_doan.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPizzaActivity extends AppCompatActivity {

    private ImageView imagePizza;
    private TextView txtName, txtPrice, txtDescription;
    private Spinner spinnerSize, spinnerDe;
    private RecyclerView recyclerToppings;
    private ToppingAdapter toppingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pizza);

        // Ánh xạ view
        imagePizza = findViewById(R.id.image_pizza);
        txtName = findViewById(R.id.txt_pizza_name);
        txtPrice = findViewById(R.id.text_price);
        txtDescription = findViewById(R.id.text_description);
        spinnerSize = findViewById(R.id.spinner_size);
        spinnerDe = findViewById(R.id.spinner_de);
        recyclerToppings = findViewById(R.id.recycler_toppings);

        // Cấu hình RecyclerView
        recyclerToppings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        toppingAdapter = new ToppingAdapter(new ArrayList<>());
        recyclerToppings.setAdapter(toppingAdapter);

        // Lấy pizza từ intent
        Pizza pizza = (Pizza) getIntent().getSerializableExtra("pizza");

        if (pizza != null) {
            txtName.setText(pizza.getName());
            txtPrice.setText("$" + pizza.getBasePrice());
            txtDescription.setText(pizza.getDescription());

            Glide.with(this)
                    .load(pizza.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imagePizza);

            // Load topping
            loadToppingsFromServer(pizza);
            setupSpinners(pizza);
        }
        // Nút quay lại
        ImageView imgBack = findViewById(R.id.image_arrow_left);
        imgBack.setOnClickListener(v -> finish());
        imgBack.setOnTouchListener((v, event) -> {
            v.setAlpha(event.getAction() == MotionEvent.ACTION_DOWN ? 0.6f : 1.0f);
            return false;
        });

        // kiem tra topping dang co trong pizza
        if (pizza != null) {
            //  Log kiểm tra toppings từ pizza
            Log.d("DEBUG_PIZZA", "Pizza toppings received: " + pizza.getToppings());
            if (pizza.getToppings() != null) {
                for (String topping : pizza.getToppings()) {
                    Log.d("DEBUG_PIZZA", "Pizza has topping: " + topping);
                }
            } else {
                Log.d("DEBUG_PIZZA", "Pizza toppings is null");
            }

            txtName.setText(pizza.getName());
            txtPrice.setText("$" + pizza.getBasePrice());
            txtDescription.setText(pizza.getDescription());

            Glide.with(this)
                    .load(pizza.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imagePizza);

            // Load topping
            loadToppingsFromServer(pizza);
        }

        // xử lý btn add to cart
        findViewById(R.id.text_add_to_cart).setOnClickListener(v -> {
            if (pizza == null) return;

            String selectedSize = spinnerSize.getSelectedItem().toString().replace("Size ", "");
            String selectedCrust = spinnerDe.getSelectedItem().toString();
            List<String> selectedToppings = toppingAdapter.getSelectedToppingIds();

            // Tạo customPizza
            AddToCartRequest.CustomPizza customPizza = new AddToCartRequest.CustomPizza(
                    pizza.getId(),
                    selectedSize,
                    selectedCrust,
                    selectedToppings,
                    1
            );

            AddToCartRequest.PizzaData pizzaData = new AddToCartRequest.PizzaData(customPizza, 1);
            AddToCartRequest request = new AddToCartRequest(pizzaData, null, null, null);

            // Lấy token từ SharedPreferences
            String token = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("token", null);
            if (token == null) {
                Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                return;
            }

            String bearerToken = "Bearer " + token;

            RetrofitClient.getApiService().addToCart(bearerToken, request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String message = "Pizza \"" + pizza.getName() + "\" đã được thêm vào giỏ hàng!\n"
                                + "Kích cỡ: " + selectedSize
                                + ", Đế: " + selectedCrust
                                + ", Topping: " + selectedToppings.size() + " món";
                        Toast.makeText(DetailPizzaActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DetailPizzaActivity.this, " Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                        Log.e("ADD_TO_CART_FAIL", "Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(DetailPizzaActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("ADD_TO_CART_ERROR", t.getMessage(), t);
                }
            });
        });
    }
    private void loadToppingsFromServer(Pizza pizza) {
        RetrofitClient.getApiService().getAllToppings().enqueue(new Callback<List<Topping>>() {
            @Override
            public void onResponse(Call<List<Topping>> call, Response<List<Topping>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Topping> allToppings = response.body();
                    Log.d("TOPPING_DEBUG", "Tổng số toppings từ server: " + allToppings.size());
                    for (Topping topping : allToppings) {
                        Log.d("TOPPING_DEBUG", "Topping: " + topping.getName() + " - " + topping.getImageUrl());
                    }

                    toppingAdapter.updateToppings(allToppings); //  Hiển thị tất cả topping
                } else {
                    Toast.makeText(DetailPizzaActivity.this, "Không nhận được dữ liệu topping", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Topping>> call, Throwable t) {
                Toast.makeText(DetailPizzaActivity.this, "Lỗi tải topping: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TOPPING_ERROR", t.getMessage(), t);
            }
        });
    }
    private void setupSpinners(Pizza pizza) {
        // Size spinner
        String[] sizes = {"Size S", "Size M", "Size L"};
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizes);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(sizeAdapter);

        // Crust spinner
        String[] crusts = {"Mỏng", "Vừa", "Dày"};
        ArrayAdapter<String> crustAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, crusts);
        crustAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDe.setAdapter(crustAdapter);

        // Set default selections from pizza object (nếu có)
        if (pizza != null) {
            int sizeIndex = sizeAdapter.getPosition("Size " + pizza.getSize());
            int crustIndex = crustAdapter.getPosition(pizza.getCrustType());
            if (sizeIndex >= 0) spinnerSize.setSelection(sizeIndex);
            if (crustIndex >= 0) spinnerDe.setSelection(crustIndex);
        }
    }

}
