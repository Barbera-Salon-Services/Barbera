package com.barbera.barberaconsumerapp;

import java.util.List;

public class WeddingModel {
    private String PackageName;
    private String PackageContent;
    private String PackagePrice;
    private String type;
    public List<String> sittings;

    public WeddingModel(String packageName, String packageContent, String packagePrice,String itype,List<String> stringList) {
        PackageName = packageName;
        PackageContent = packageContent;
        PackagePrice = packagePrice;
        type=itype;
        sittings=stringList;
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