package com.example.android_doan.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.android_doan.R;

public class TrackingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_tracking, container, false);
        ConstraintLayout layout = view.findViewById(R.id.fragment_tracking);
        layout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
        return view;
    }

    public void updateDeliveryStatus(String status) {
        if (getView() != null) {
            TextView txt = getView().findViewById(R.id.txt_giao_hang_status);
            txt.setText(status);
        }
    }


}