package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TypeList {
    @SerializedName("data")
    List<String> typeList;

    public TypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public List<String> getTypeList() {
        return typeList;
    }
}
