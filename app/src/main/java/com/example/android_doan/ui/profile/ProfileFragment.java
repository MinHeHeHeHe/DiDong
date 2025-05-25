package com.example.android_doan.ui.profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android_doan.Activity.DangNhapActivity;
import com.example.android_doan.LoginResponse;
import com.example.android_doan.ThemThongTinCaNhanResponse;
import com.example.android_doan.databinding.FragmentProfileBinding;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Gọi API để lấy lại thông tin user kèm ảnh avatar (luôn mới nhất)
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = "Bearer " + prefs.getString("token", "");
        String userId = prefs.getString("userId", "");

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getUserById(token, userId).enqueue(new Callback<LoginResponse.User>() {
            @Override
            public void onResponse(Call<LoginResponse.User> call, Response<LoginResponse.User> response) {
                if (response.isSuccessful()) {
                    LoginResponse.User user = response.body();
                    if (user != null && user.getImageUrl() != null) {
                        prefs.edit().putString("image_url", user.getImageUrl()).apply();
                        Glide.with(ProfileFragment.this)
                                .load(user.getImageUrl())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(binding.shapeableImageView);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse.User> call, Throwable t) {
                Log.e("PROFILE_ERROR", "Failed to fetch user info", t);
            }
        });


        // Xin quyền truy cập ảnh nếu chưa có
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 101);
            }
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
            }
        }

        // Sự kiện nhấn chỉnh sửa hồ sơ
        binding.textChinhSuaThongTin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChinhSuaThongTinActivity.class);
            startActivity(intent);
        });

        // Sự kiện nhấn avatar để chọn ảnh
        binding.shapeableImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Sự kiện nhấn đăng xuất
        binding.textDangXuat.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE)
                        .edit()
                        .remove("token")
                        .apply();
            }
            Intent intent = new Intent(getActivity(), DangNhapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            binding.shapeableImageView.setImageURI(imageUri);
            uploadAvatarOnly(imageUri);
        }
    }

    private void uploadAvatarOnly(Uri imageUri) {
        try {
            String mimeType = getContext().getContentResolver().getType(imageUri);
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);

            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), imageBytes);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "avatar.jpg", requestFile);

            SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String token = "Bearer " + prefs.getString("token", "");
            String userId = prefs.getString("userId", "");

            ApiService apiService = RetrofitClient.getApiService();
            Call<ThemThongTinCaNhanResponse> call = apiService.updateUserAvatar(token, userId, imagePart);

            call.enqueue(new Callback<ThemThongTinCaNhanResponse>() {
                @Override
                public void onResponse(Call<ThemThongTinCaNhanResponse> call, Response<ThemThongTinCaNhanResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show();
                        ThemThongTinCaNhanResponse.User updatedUser = response.body().getUser();
                        if (updatedUser != null && updatedUser.getImageUrl() != null) {
                            prefs.edit().putString("image_url", updatedUser.getImageUrl()).apply();
                            Glide.with(getContext())
                                    .load(updatedUser.getImageUrl())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.shapeableImageView);
                        }
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("UPLOAD_RESPONSE_ERROR", errorBody);
                            Toast.makeText(getContext(), "Lỗi server: " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ThemThongTinCaNhanResponse> call, Throwable t) {
                    Log.e("UPLOAD_ERROR", "Upload failed", t);
                    Toast.makeText(getContext(), "Upload ảnh thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("UPLOAD_EXCEPTION", "Exception: ", e);
            Toast.makeText(getContext(), "Không đọc được ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == 101 || requestCode == 102) && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Đã cấp quyền truy cập ảnh", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Từ chối quyền, không thể chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
