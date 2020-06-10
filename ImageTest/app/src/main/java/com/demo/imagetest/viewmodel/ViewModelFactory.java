package com.demo.imagetest.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory{
    private Context context;

    public ViewModelFactory(Context context){
        this.context = context;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ImageViewModel.class)) {
            return (T) new ImageViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}