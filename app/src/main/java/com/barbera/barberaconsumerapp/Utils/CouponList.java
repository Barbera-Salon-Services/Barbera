package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CouponList {
    @SerializedName("data")
    private List<GetCouponItem> list;
    @SerializedName("refquantity")
    private int refQuantity;

    public CouponList(List<GetCouponItem> list, int refQuantity) {
        this.list = list;
        this.refQuantity = refQuantity;
    }

    public int getRefQuantity() {
        return refQuantity;
    }

    public List<GetCouponItem> getList() {
        return list;
    }
}
