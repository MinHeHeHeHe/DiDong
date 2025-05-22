package com.example.android_doan.Activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
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
    private ImageView[] toppingImageViews;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pizza);

        // Khởi tạo view
        imagePizza = findViewById(R.id.image_pizza);
        txtName = findViewById(R.id.txt_pizza_name);
        txtPrice = findViewById(R.id.text_price);
        txtDescription = findViewById(R.id.text_description);
        spinnerSize = findViewById(R.id.spinner_size);
        spinnerDe = findViewById(R.id.spinner_de);

        toppingImageViews = new ImageView[]{
                findViewById(R.id.image_mushroom),
                findViewById(R.id.image_onion),
                findViewById(R.id.image_pepperoni),
                findViewById(R.id.image_spinach),
                findViewById(R.id.image_pineapple)
        };

        // Nhận pizza từ Intent
        Pizza pizza = (Pizza) getIntent().getSerializableExtra("pizza");

        if (pizza != null) {
            txtName.setText(pizza.getName());
            txtPrice.setText("$" + pizza.getBasePrice());
            txtDescription.setText(pizza.getDescription());

            Glide.with(this)
                    .load(pizza.getImageUrl())
                    .into(imagePizza);

            // Thêm đoạn này tại đây để hiển thị topping
            RetrofitClient.getApiService().getAllToppings().enqueue(new Callback<List<Topping>>() {
                @Override
                public void onResponse(Call<List<Topping>> call, Response<List<Topping>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<String> pizzaToppingNames = pizza.getToppings();
                        List<Topping> allToppings = response.body();

                        List<Topping> selectedToppings = new ArrayList<>();
                        for (Topping topping : allToppings) {
                            if (pizzaToppingNames.contains(topping.getName())) {
                                selectedToppings.add(topping);
                            }
                        }

                        for (int i = 0; i < Math.min(5, selectedToppings.size()); i++) {
                            Topping topping = selectedToppings.get(i);
                            ImageView imageView = toppingImageViews[i];
                            Glide.with(DetailPizzaActivity.this)
                                    .load(topping.getImageUrl())
                                    .into(imageView);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Topping>> call, Throwable t) {
                    Toast.makeText(DetailPizzaActivity.this, "Không thể tải topping", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Quay lại
        ImageView imgBack = findViewById(R.id.image_arrow_left);
        imgBack.setOnClickListener(v -> finish());
        imgBack.setOnTouchListener((v, event) -> {
            v.setAlpha(event.getAction() == MotionEvent.ACTION_DOWN ? 0.6f : 1.0f);
            return false;
        });
    }

}
