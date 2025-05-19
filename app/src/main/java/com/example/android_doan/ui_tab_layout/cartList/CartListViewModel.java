package com.example.android_doan.ui_tab_layout.cartList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartListViewModel extends ViewModel {

    // Dữ liệu giỏ hàng dưới dạng danh sách (có thể thay bằng đối tượng CartItem nếu bạn có)
    private final MutableLiveData<List<String>> cartItems;

    public CartListViewModel() {
        cartItems = new MutableLiveData<>(new ArrayList<>());
    }

    // Trả dữ liệu giỏ hàng dưới dạng LiveData để Fragment quan sát
    public LiveData<List<String>> getCartItems() {
        return cartItems;
    }

    // Thêm sản phẩm vào giỏ
    public void addItem(String item) {
        List<String> currentList = cartItems.getValue();
        if (currentList != null) {
            currentList.add(item);
            cartItems.setValue(currentList);
        }
    }

    // Xóa sản phẩm khỏi giỏ
    public void removeItem(String item) {
        List<String> currentList = cartItems.getValue();
        if (currentList != null) {
            currentList.remove(item);
            cartItems.setValue(currentList);
        }
    }

    // Xóa toàn bộ giỏ hàng
    public void clearCart() {
        cartItems.setValue(new ArrayList<>());
    }
}
