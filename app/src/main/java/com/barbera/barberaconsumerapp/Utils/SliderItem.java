package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

public class SliderItem {
    @SerializedName("types")
    private String types;
    @SerializedName("category")
    private String category;
    @SerializedName("image")
    private String url;
    @SerializedName("name")
    private String name;
    @SerializedName("clickable")
    private boolean clickable;

    public SliderItem(String types, String category,String url,String name,boolean clickable) {
        this.types = types;
        this.category = category;
        this.url=url;
        this.name=name;
        this.clickable=clickable;
    }

    public boolean isClickable() {
        return clickable;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public String getTypes() {
        return types;
    }
}
