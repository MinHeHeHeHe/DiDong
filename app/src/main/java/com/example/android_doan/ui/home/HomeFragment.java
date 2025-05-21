package com.example.android_doan.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android_doan.R;
import com.example.android_doan.adapter.DrinkAdapter;
import com.example.android_doan.adapter.PizzaAdapter;
import com.example.android_doan.databinding.FragmentHomeBinding;
import com.example.android_doan.model.Drink;
import com.example.android_doan.model.Pizza;
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
    private CardView selectedCard = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerList;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        pizzaAdapter = new PizzaAdapter(new ArrayList<>());
        drinkAdapter = new DrinkAdapter(new ArrayList<>());
        recyclerView.setAdapter(pizzaAdapter); // mặc định là Pizza

        // Gọi API Pizza mặc định
        fetchPizzas();

        // Thiết lập sự kiện cho từng loại
        setupCategorySelection(binding.btnPizza, binding.cardPizza, "pizza");
        setupCategorySelection(binding.btnDrink, binding.cardDrink, "drink");

        // Gọi chọn mặc định Pizza
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

                // Tải dữ liệu tương ứng
                if ("pizza".equals(type)) {
                    binding.recyclerList.setAdapter(pizzaAdapter);
                    fetchPizzas();
                } else if ("drink".equals(type)) {
                    binding.recyclerList.setAdapter(drinkAdapter);
                    fetchDrinks();
                }
            }
        });
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
