package com.example.android_doan.ui.orders;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrderPagerAdapter extends FragmentStateAdapter {

    public OrderPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new CartFragment();      // Giỏ hàng
            case 1: return new PaymentFragment();   // Thanh toán
            case 2: return new TrackingFragment();  // Theo dõi
            default: return new CartFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
