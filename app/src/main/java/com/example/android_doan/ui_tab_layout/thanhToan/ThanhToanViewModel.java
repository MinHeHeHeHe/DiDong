package com.example.android_doan.ui_tab_layout.thanhToan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ThanhToanViewModel extends ViewModel {

        private final MutableLiveData<String> mText;

        public ThanhToanViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("This is thanh toan fragment");
        }

        public LiveData<String> getText() {
            return mText;
        }
    }
