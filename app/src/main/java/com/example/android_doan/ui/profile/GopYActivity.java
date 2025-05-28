package com.example.android_doan.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_doan.CommentRequest;
import com.example.android_doan.R;
import com.example.android_doan.network.ApiService;
import com.example.android_doan.network.RetrofitClient;

import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GopYActivity extends AppCompatActivity {

    private EditText edtNote;
    private final Map<CheckBox, String> checkboxMap = new LinkedHashMap<>();
    private final LinkedHashMap<CheckBox, Long> checkedOrder = new LinkedHashMap<>();
    private boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gop_y);

        Button btnSubmit = findViewById(R.id.button_submit_feedback);
        edtNote = findViewById(R.id.container_frame);

        // Ánh xạ các checkbox
        CheckBox cbAppError = findViewById(R.id.checkbox_app_error);
        CheckBox cbLateDelivery = findViewById(R.id.checkbox_late_delivery);
        CheckBox cbBadQuality = findViewById(R.id.checkbox_bad_quality);
        CheckBox cbWrongOrder = findViewById(R.id.checkbox_wrong_order);
        CheckBox cbStaffAttitude = findViewById(R.id.checkbox_staff_attitude);

        checkboxMap.put(cbAppError, "- App bị lỗi");
        checkboxMap.put(cbLateDelivery, "- Giao hàng chậm trễ");
        checkboxMap.put(cbBadQuality, "- Món ăn không đạt chất lượng");
        checkboxMap.put(cbWrongOrder, "- Sai đơn hàng");
        checkboxMap.put(cbStaffAttitude, "- Thái độ nhân viên không phù hợp");

        for (CheckBox checkBox : checkboxMap.keySet()) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedOrder.put(checkBox, System.nanoTime());
                } else {
                    checkedOrder.remove(checkBox);
                }
                updateAutoText();
            });
        }

        edtNote.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (!isUpdating) {
                    // Người dùng tự gõ — không can thiệp
                }
            }
        });

        btnSubmit.setOnClickListener(v -> submitFeedback());
    }

    private void updateAutoText() {
        isUpdating = true;
        String currentText = edtNote.getText().toString();
        String[] lines = currentText.split("\n");

        Map<String, String> existingLines = new LinkedHashMap<>();
        StringBuilder userBuilder = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("-")) {
                String matched = checkboxMap.values().stream()
                        .filter(d -> trimmed.startsWith(d))
                        .findFirst().orElse(null);

                if (matched != null) {
                    String label = matched.replaceFirst("^-\\s*", "");
                    existingLines.put(label, trimmed);
                    continue;
                }
            }
            userBuilder.append(line).append("\n");
        }

        StringBuilder autoBuilder = new StringBuilder();
        checkedOrder.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> {
                    String defaultText = checkboxMap.get(entry.getKey());
                    String label = defaultText.replaceFirst("^-\\s*", "");
                    String original = existingLines.getOrDefault(label, defaultText);
                    autoBuilder.append(original).append("\n");
                });

        String autoText = autoBuilder.toString().trim();
        String userText = userBuilder.toString().trim();

        StringBuilder finalText = new StringBuilder();
        if (!autoText.isEmpty()) finalText.append(autoText);
        if (!userText.isEmpty()) {
            if (!autoText.isEmpty()) finalText.append("\n\n");
            finalText.append(userText);
        }

        edtNote.setText(finalText.toString().trim());
        edtNote.setSelection(finalText.length());
        isUpdating = false;
    }

    private void submitFeedback() {
        String comment = edtNote.getText().toString().trim();
        if (comment.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập góp ý!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        String deliveryId = prefs.getString("deliveryId", ""); //  đảm bảo key đúng với MapFragment

        if (token.isEmpty() || deliveryId.isEmpty()) {
            Toast.makeText(this, "Thiếu token hoặc deliveryId", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.addComment("Bearer " + token, deliveryId, new CommentRequest(comment));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GopYActivity.this, "Gửi góp ý thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // trở về HomeFragment hoặc nơi trước đó
                } else {
                    Toast.makeText(GopYActivity.this, "Gửi thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(GopYActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
