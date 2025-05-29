package com.example.android_doan.ui.map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android_doan.R;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;
import com.example.android_doan.ui.orders.TrackingFragment;
import com.example.android_doan.ui.profile.GopYActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private Button btnConfirm;
    private MapView mapView;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        mapView = view.findViewById(R.id.map_view);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        btnConfirm.setOnClickListener(v -> confirmDelivery());
        return view;
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        this.googleMap = gMap;

        // 📍 Thay thế tọa độ này bằng vị trí thực tế nếu bạn có
        LatLng restaurant = new LatLng(10.762622, 106.660172); // Nhà hàng
        LatLng user = new LatLng(10.870028, 106.803040); // Khách hàng

        googleMap.addMarker(new MarkerOptions().position(restaurant).title("Nhà hàng"));
        googleMap.addMarker(new MarkerOptions().position(user).title("Khách hàng"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurant, 11));
    }

    private void confirmDelivery() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String deliveryId = prefs.getString("lastDeliveryId", "");

        if (token.isEmpty() || deliveryId.isEmpty()) {
            Toast.makeText(getContext(), "Thiếu token hoặc deliveryId", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        apiService.confirmDelivery("Bearer " + token, deliveryId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "✅ Đã xác nhận giao hàng", Toast.LENGTH_SHORT).show();
                    // ✅ Lưu deliveryId để GopYActivity sử dụng
                    prefs.edit().putString("deliveryId", deliveryId).apply();

                    FragmentManager fm = requireActivity().getSupportFragmentManager();
                    for (Fragment fragment : fm.getFragments()) {
                        if (fragment instanceof TrackingFragment && fragment.getView() != null) {
                            ((MapFragment) fragment).updateDeliveryStatus("Đã giao");
                            break;
                        }
                    }
                    // 👇 Mở GopYActivity
                    startActivity(new Intent(requireContext(), GopYActivity.class));
                } else {
                    Toast.makeText(getContext(), "❌ Xác nhận thất bại - Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "❌ Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateDeliveryStatus(String status) {
        if (getView() != null) {
            TextView txt = getView().findViewById(R.id.txt_giao_hang_status);
            txt.setText(status);
        }
    }

    // Lifecycle bắt buộc cho MapView
    @Override public void onResume() { super.onResume(); mapView.onResume(); }
    @Override public void onPause() { super.onPause(); mapView.onPause(); }
    @Override public void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
}
