package com.example.android_doan.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.Activity.DangNhapActivity;
import com.example.android_doan.databinding.FragmentProfileBinding;
import android.content.Intent;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Sự kiện nhấn chỉnh sửa hồ sơ
        binding.textChinhSuaThongTin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChinhSuaThongTinActivity.class);
            startActivity(intent);
        });



        // Sự kiện nhấn đăng xuất
        binding.textDangXuat.setOnClickListener(v -> {
            // Xoá token khỏi SharedPreferences
            if (getActivity() != null) {
                getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE)
                        .edit()
                        .remove("token")  // hoặc clear() nếu muốn xoá tất cả
                        .apply();
            }

            Intent intent = new Intent(getActivity(), DangNhapActivity.class);
            // Xóa hết các activity trước đó để không quay lại bằng nút back
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
