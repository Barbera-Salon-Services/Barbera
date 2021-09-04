package com.barbera.barberaconsumerapp.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServiceViewModel  extends AndroidViewModel {
    private ServiceRepository repository;
    private LiveData<List<ServiceEntity>> allServices;
    public ServiceViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public LiveData<List<ServiceEntity>> getAllServices() {
        return allServices;
    }
}
