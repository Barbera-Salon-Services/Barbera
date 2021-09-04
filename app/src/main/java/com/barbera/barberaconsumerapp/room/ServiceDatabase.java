package com.barbera.barberaconsumerapp.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = ServiceEntity.class,version = 1)
public abstract class ServiceDatabase extends RoomDatabase {
    private static ServiceDatabase instance;

    public abstract ServiceDAO serviceDAO();

    public static synchronized ServiceDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    ServiceDatabase.class,"service_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
