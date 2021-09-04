package com.barbera.barberaconsumerapp.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "service_table")
public class ServiceEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String price;
    private String description;
    private String time;

    public ServiceEntity(String title, String price, String description, String time) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }
}
