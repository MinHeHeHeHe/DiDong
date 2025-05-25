package com.example.android_doan.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.android_doan.databinding.FragmentOrdersBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Cài adapter cho ViewPager2
        OrderPagerAdapter adapter = new OrderPagerAdapter(this); // sử dụng FragmentStateAdapter
        binding.viewPager.setAdapter(adapter);

        // Kết nối TabLayout với ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (TabLayout.Tab tab, int position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Giỏ hàng");
                            break;
                        case 1:
                            tab.setText("Thanh toán");
                            break;
                        case 2:
                            tab.setText("Theo dõi");
                            break;
                    }
                }).attach();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
