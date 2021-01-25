package com.barbera.barberaconsumerapp;

public class WeddingModel {
    private String PackageName;
    private String PackageContent;
    private String PackagePrice;
    private String type;
    private String icon;
    private String cutPrice;

    public WeddingModel(String packageName, String packageContent, String packagePrice,String itype,String mcutPrice,String micon) {
        PackageName = packageName;
        PackageContent = packageContent;
        PackagePrice = packagePrice;
        type=itype;
        cutPrice=mcutPrice;
        icon=micon;
    }

    public String getCutPrice() {
        return cutPrice;
    }

    public String getIcon() {
        return icon;
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
