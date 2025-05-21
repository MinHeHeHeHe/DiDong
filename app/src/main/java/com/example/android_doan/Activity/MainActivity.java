package com.example.android_doan.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.android_doan.databinding.ActivityMainBinding;
import com.example.android_doan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set toolbar (nếu có)
        setSupportActionBar(binding.toolbar);

        // Khởi tạo NavController với ID đúng
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_ho_so_nguoi_dung);

        // Định nghĩa các điểm đầu cuối trong nav graph
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_orders, R.id.navigation_profile)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}
