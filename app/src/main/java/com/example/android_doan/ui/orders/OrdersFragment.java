package com.example.android_doan.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.databinding.FragmentOrdersBinding;
import com.example.android_doan.ui.orders.OrdersViewModel;

public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OrdersViewModel ordersViewModel =
                new ViewModelProvider(this).get(OrdersViewModel.class);

        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Làm mờ layout nếu cần
        final ConstraintLayout layout = binding.textOrders;
        ordersViewModel.getText().observe(getViewLifecycleOwner(), text -> {
            layout.setAlpha(0.5f);
        });

        //  Thêm các tab vào TabLayout

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Danh sách"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Thanh toán"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Theo dõi"));

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}