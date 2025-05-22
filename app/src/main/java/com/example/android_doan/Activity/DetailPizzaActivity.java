package com.example.android_doan.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.adapter.ToppingAdapter;
import com.example.android_doan.model.Pizza;
import com.example.android_doan.model.Topping;
import com.example.android_doan.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

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
}
