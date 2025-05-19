package com.example.android_doan.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.android_doan.ui_tab_layout.cartList.CartListFragment;
import com.example.android_doan.ui_tab_layout.thanhToan.ThanhToanFragment;
import com.example.android_doan.ui_tab_layout.theoDoi.TheoDoiFragment;

public class OrderPagerAdapter extends FragmentStateAdapter {

    // Constructor – nhận vào FragmentActivity chứa ViewPager2
    public OrderPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // Trả về Fragment tương ứng với vị trí (tab)
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CartListFragment();   // Tab 0 - Cart
            case 1:
                return new ThanhToanFragment();  // Tab 1 - Thanh toán
            case 2:
                return new TheoDoiFragment();    // Tab 2 - Theo dõi
            default:
                return new CartListFragment();   // Mặc định trả về Cart
        }
    }

    // Trả về số lượng Fragment (số tab)
    @Override
    public int getItemCount() {
        return 3;
    }
}
