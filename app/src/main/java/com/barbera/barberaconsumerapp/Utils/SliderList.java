package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SliderList {
    @SerializedName("data")
    private List<SliderItem> list;

    public SliderList(List<SliderItem> list) {
        this.list = list;
    }

    public List<SliderItem> getList() {
        return list;
    }
}
