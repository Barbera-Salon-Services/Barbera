package com.barbera.barberaconsumerapp;

import java.util.List;

public class WeddingModel {
    private String PackageName;
    private String PackageContent;
    private int PackagePrice;

    public WeddingModel(String packageName, String packageContent, int packagePrice) {
        PackageName = packageName;
        PackageContent = packageContent;
        PackagePrice = packagePrice;
    }

    public String getPackageName() {
        return PackageName;
    }

    public String getPackageContent() {
        return PackageContent;
    }

    public int getPackagePrice() {
        return PackagePrice;
    }
}