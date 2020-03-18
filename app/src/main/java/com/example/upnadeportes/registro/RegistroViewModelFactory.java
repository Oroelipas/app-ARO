package com.example.upnadeportes.registro;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class RegistroViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegistroViewModel.class)) {
            return (T) new RegistroViewModel();
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
