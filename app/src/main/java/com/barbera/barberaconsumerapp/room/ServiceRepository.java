package com.barbera.barberaconsumerapp.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ServiceRepository {
    private ServiceDAO serviceDAO;
    private LiveData<List<ServiceEntity>> allServices;

    public ServiceRepository(Application application){
        ServiceDatabase database= ServiceDatabase.getInstance(application);
        serviceDAO=database.serviceDAO();
        allServices=serviceDAO.getAllServices();
    }
    public LiveData<List<ServiceEntity>> getAllServices(){
        return allServices;
    }
}
