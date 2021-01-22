package com.barbera.barberaconsumerapp;

public class WeddingModel {
    private String PackageName;
    private String PackageContent;
    private String PackagePrice;
    private String type;

    public WeddingModel(String packageName, String packageContent, String packagePrice,String itype) {
        PackageName = packageName;
        PackageContent = packageContent;
        PackagePrice = packagePrice;
        type=itype;
    }

    public String getPackageName() {
        return PackageName;
    }

    public String getPackageContent() {
        return PackageContent;
    }

    public String getPackagePrice() {
        return PackagePrice;
    }
    public String getType(){
        return type;
    }
}
