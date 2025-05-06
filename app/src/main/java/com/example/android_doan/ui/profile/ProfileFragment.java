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

        // Ví dụ cập nhật nội dung text bằng ViewModel
        binding.textChinhSuaThongTin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChinhSuaThongTinActivity.class);
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
