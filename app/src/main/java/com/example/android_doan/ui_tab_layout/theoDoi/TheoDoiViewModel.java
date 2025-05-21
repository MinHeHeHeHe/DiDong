package com.example.android_doan.ui_tab_layout.theoDoi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TheoDoiViewModel extends ViewModel {

        private final MutableLiveData<String> mText;

        public TheoDoiViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("This is theo doi fragment");
        }

        public LiveData<String> getText() {
            return mText;
        }
    }
