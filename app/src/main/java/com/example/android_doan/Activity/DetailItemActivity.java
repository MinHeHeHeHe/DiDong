package com.example.android_doan.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.android_doan.AddToCartRequest;
import com.example.android_doan.R;
import com.example.android_doan.model.Drink;
import com.example.android_doan.model.Salad;
import com.example.android_doan.model.SideDish;
import com.example.android_doan.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailItemActivity extends AppCompatActivity {

    private ImageView imageItem, btnBack;
    private TextView txtName, txtPrice, txtDescription;

    private Drink drink;
    private SideDish sideDish;
    private Salad salad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        imageItem = findViewById(R.id.image_item);
        btnBack = findViewById(R.id.image_arrow_left);
        txtName = findViewById(R.id.txt_item_name);
        txtPrice = findViewById(R.id.text_item_price);
        txtDescription = findViewById(R.id.text_item_description);

        btnBack.setOnClickListener(v -> finish());
        btnBack.setOnTouchListener((v, event) -> {
            v.setAlpha(event.getAction() == MotionEvent.ACTION_DOWN ? 0.6f : 1.0f);
            return false;
        });

        Object item = getIntent().getSerializableExtra("item");

        // Nút quay lại
        ImageView imgBack = findViewById(R.id.image_arrow_left);
        imgBack.setOnClickListener(v -> finish());
        imgBack.setOnTouchListener((v, event) -> {
            v.setAlpha(event.getAction() == MotionEvent.ACTION_DOWN ? 0.6f : 1.0f);
            return false;
        });


        if (item instanceof Drink) {
            drink = (Drink) item;
            bindData(drink.getName(), drink.getBasePrice(), drink.getDescription(), drink.getImageUrl());
        } else if (item instanceof SideDish) {
            sideDish = (SideDish) item;
            bindData(sideDish.getName(), sideDish.getBasePrice(), sideDish.getDescription(), sideDish.getImageUrl());
        } else if (item instanceof Salad) {
            salad = (Salad) item;
            bindData(salad.getName(), salad.getBasePrice(), salad.getDescription(), salad.getImageUrl());
        } else {
            Toast.makeText(this, "Không có dữ liệu chi tiết", Toast.LENGTH_SHORT).show();
            finish();
        }

        findViewById(R.id.text_add_to_cart).setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String token = prefs.getString("token", null);
            if (token == null) {
                Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                return;
            }

            AddToCartRequest request = new AddToCartRequest(null, null, null, null);

            if (drink != null) {
                AddToCartRequest.DrinkData drinkData = new AddToCartRequest.DrinkData(drink.getId(), 1);
                request.setDrinks(java.util.Collections.singletonList(drinkData));
            } else if (sideDish != null) {
                AddToCartRequest.SideData sideData = new AddToCartRequest.SideData(sideDish.getId(), 1);
                request.setSides(java.util.Collections.singletonList(sideData));
            } else if (salad != null) {
                AddToCartRequest.SaladData saladData = new AddToCartRequest.SaladData(salad.getId(), 1);
                request.setSalads(java.util.Collections.singletonList(saladData));
            }

            String bearerToken = "Bearer " + token;

            RetrofitClient.getApiService().addToCart(bearerToken, request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String itemName = "";
                        if (drink != null) {
                            itemName = drink.getName();
                        } else if (sideDish != null) {
                            itemName = sideDish.getName();
                        } else if (salad != null) {
                            itemName = salad.getName();
                        }
                        Toast.makeText(DetailItemActivity.this, "Đã thêm \"" + itemName + "\" vào giỏ hàng!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(DetailItemActivity.this, "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                        Log.e("ADD_TO_CART_FAIL", "Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(DetailItemActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("ADD_TO_CART_ERROR", t.getMessage(), t);
                }
            });
        });
    }

    private void bindData(String name, double price, String description, String imageUrl) {
        txtName.setText(name);
        txtPrice.setText("$" + price);
        txtDescription.setText(description);
        Glide.with(this).load(imageUrl).into(imageItem);
    }
}
