package com.barbera.barberaconsumerapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ServiceDAO {
    @Query("SELECT * FROM service_table")
    LiveData<List<ServiceEntity>> getAllServices();

}
