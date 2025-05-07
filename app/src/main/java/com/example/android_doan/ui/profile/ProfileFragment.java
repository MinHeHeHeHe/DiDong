package com.example.android_doan.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_doan.databinding.FragmentProfileBinding;
import android.content.Intent;
import com.example.android_doan.ui.profile.ChinhSuaThongTinActivity;


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

        //Sự kiện nhấn chỉnh góp ý
        binding.textGopY.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GopYActivity.class);
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
