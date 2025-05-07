package com.example.android_doan;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView pizzaRecyclerView;
    private PizzaAdapter pizzaAdapter;
    private EditText nameInput, descriptionInput, sizeInput, crustTypeInput, toppingsInput, priceInput;
    private Button refreshButton, createPizzaButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        pizzaRecyclerView = findViewById(R.id.pizzaRecyclerView);
        nameInput = findViewById(R.id.nameInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        sizeInput = findViewById(R.id.sizeInput);
        crustTypeInput = findViewById(R.id.crustTypeInput);
        toppingsInput = findViewById(R.id.toppingsInput);
        priceInput = findViewById(R.id.priceInput);
        refreshButton = findViewById(R.id.refreshButton);
        createPizzaButton = findViewById(R.id.createPizzaButton);

        // Set up RecyclerView
        pizzaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pizzaAdapter = new PizzaAdapter(Arrays.asList());
        pizzaRecyclerView.setAdapter(pizzaAdapter);

        // Initialize Retrofit
        apiService = RetrofitClient.getApiService();

        // Load pizzas on start
        fetchPizzas();

        // Button listeners
        refreshButton.setOnClickListener(v -> fetchPizzas());
        createPizzaButton.setOnClickListener(v -> createPizza());
    }

    private void fetchPizzas() {
        apiService.getAllPizzas().enqueue(new Callback<List<Pizza>>() {
            @Override
            public void onResponse(Call<List<Pizza>> call, Response<List<Pizza>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pizzaAdapter.updatePizzas(response.body());
                    Toast.makeText(MainActivity.this, "Pizzas loaded: " + response.body().size(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load pizzas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pizza>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createPizza() {
        String name = nameInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String size = sizeInput.getText().toString();
        String crustType = crustTypeInput.getText().toString();
        String toppings = toppingsInput.getText().toString();
        String price = priceInput.getText().toString();

        if (name.isEmpty() || description.isEmpty() || size.isEmpty() || crustType.isEmpty() || toppings.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Pizza pizza = new Pizza();
        pizza.setName(name);
        pizza.setDescription(description);
        pizza.setSize(size);
        pizza.setCrustType(crustType);
        pizza.setToppings(Arrays.asList(toppings.split(",")));
        pizza.setBasePrice(Double.parseDouble(price));

        apiService.createPizza(pizza).enqueue(new Callback<Pizza>() {
            @Override
            public void onResponse(Call<Pizza> call, Response<Pizza> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Pizza created: " + response.body().getName(), Toast.LENGTH_SHORT).show();
                    fetchPizzas(); // Refresh list
                    clearInputs();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to create pizza", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pizza> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearInputs() {
        nameInput.setText("");
        descriptionInput.setText("");
        sizeInput.setText("");
        crustTypeInput.setText("");
        toppingsInput.setText("");
        priceInput.setText("");
    }
}