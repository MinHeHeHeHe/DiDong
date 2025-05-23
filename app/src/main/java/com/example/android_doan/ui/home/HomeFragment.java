package com.example.android_doan.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.adapter.DrinkAdapter;
import com.example.android_doan.adapter.PizzaAdapter;
import com.example.android_doan.adapter.SaladAdapter;
import com.example.android_doan.adapter.SideDishAdapter;
import com.example.android_doan.databinding.FragmentHomeBinding;
import com.example.android_doan.model.Drink;
import com.example.android_doan.model.Pizza;
import com.example.android_doan.model.Salad;
import com.example.android_doan.model.SideDish;
import com.example.android_doan.network.RetrofitClient;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PizzaAdapter pizzaAdapter;
    private DrinkAdapter drinkAdapter;
    private SideDishAdapter sideDishAdapter;
    private SaladAdapter saladAdapter;
    private CardView selectedCard = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Hiển thị lời chào bằng username từ SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Người dùng");
        binding.tvWelcome.setText("Chào mừng quay lại, " + username);

        String avatarUrl = prefs.getString("avatarUrl", null);
        Log.d("DEBUG", "avatarUrl từ SharedPreferences: " + avatarUrl);

        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(this).load(avatarUrl).into(binding.imgAvatar);
        } else {
            Glide.with(this).load(R.drawable.ic_profile).into(binding.imgAvatar);
        }
        // RecyclerView setup
        RecyclerView recyclerView = binding.recyclerList;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        pizzaAdapter = new PizzaAdapter(new ArrayList<>());
        drinkAdapter = new DrinkAdapter(new ArrayList<>());
        sideDishAdapter = new SideDishAdapter(new ArrayList<>());
        saladAdapter = new SaladAdapter(new ArrayList<>());

        recyclerView.setAdapter(pizzaAdapter); // mặc định là pizza
        fetchPizzas();

        // Gán sự kiện chọn danh mục
        setupCategorySelection(binding.btnPizza, binding.cardPizza, "pizza");
        setupCategorySelection(binding.btnDrink, binding.cardDrink, "drink");
        setupCategorySelection(binding.btnSide, binding.cardSide, "side");
        setupCategorySelection(binding.btnSalad, binding.cardSalad, "salad");

        // Chọn mặc định là Pizza
        binding.btnPizza.performClick();

        return root;
    }

    private void setupCategorySelection(View button, CardView cardView, String type) {
        button.setOnClickListener(v -> {
            if (selectedCard != null && selectedCard != cardView) {
                selectedCard.setCardElevation(0f);
                selectedCard.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
            }

            if (selectedCard != cardView) {
                cardView.setCardElevation(12f);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
                selectedCard = cardView;

                // Tải dữ liệu theo danh mục
                switch (type) {
                    case "pizza":
                        binding.recyclerList.setAdapter(pizzaAdapter);
                        fetchPizzas();
                        break;
                    case "drink":
                        binding.recyclerList.setAdapter(drinkAdapter);
                        fetchDrinks();
                        break;
                    case "side":
                        binding.recyclerList.setAdapter(sideDishAdapter);
                        fetchSideDishes();
                        break;
                    case "salad":
                        binding.recyclerList.setAdapter(saladAdapter);
                        fetchSalads();
                        break;
                }
            }
        });
    }

    // lay ds pizza
    private void fetchPizzas() {
        RetrofitClient.getApiService().getAllPizzas().enqueue(new Callback<List<Pizza>>() {
            @Override
            public void onResponse(Call<List<Pizza>> call, Response<List<Pizza>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pizzaAdapter.updatePizzas(response.body());
                } else {
                    Toast.makeText(getContext(), "Lỗi tải pizza", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pizza>> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể kết nối máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // lay ds drink
    private void fetchDrinks() {
        RetrofitClient.getApiService().getAllDrinks().enqueue(new Callback<List<Drink>>() {
            @Override
            public void onResponse(Call<List<Drink>> call, Response<List<Drink>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    drinkAdapter.updateDrinks(response.body());
                } else {
                    Toast.makeText(getContext(), "Lỗi tải đồ uống", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Drink>> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể kết nối máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // lay ds side dish
    private void fetchSideDishes() {
        RetrofitClient.getApiService().getAllSide().enqueue(new Callback<List<SideDish>>() {
            @Override
            public void onResponse(Call<List<SideDish>> call, Response<List<SideDish>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sideDishAdapter.updateSideDishes(response.body());
                } else {
                    Toast.makeText(getContext(), "Lỗi tải món ăn kèm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SideDish>> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể kết nối máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // lay ds salad
    private void fetchSalads() {
        RetrofitClient.getApiService().getAllSalad().enqueue(new Callback<List<Salad>>() {
            @Override
            public void onResponse(Call<List<Salad>> call, Response<List<Salad>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saladAdapter.updateSalads(response.body());
                } else {
                    Toast.makeText(getContext(), "Lỗi tải salad", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Salad>> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể kết nối máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
