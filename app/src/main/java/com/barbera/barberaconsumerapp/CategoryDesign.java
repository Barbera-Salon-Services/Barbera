package com.barbera.barberaconsumerapp;

public class CategoryDesign {
    private String CategoryIcon;
    private String CategoryName;
   // private String[] services;
    private String CategoryImage;
    private boolean hassubCategory;


    public CategoryDesign(String categoryIcon, String categoryName ,String categoryImage,boolean ihassubCategory) {
        CategoryIcon = categoryIcon;
        CategoryName = categoryName;
        CategoryImage=categoryImage;
        hassubCategory=ihassubCategory;
    }

    public String getCategoryIcon() {
        return CategoryIcon;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public boolean HavesubCategory() {
        return hassubCategory;
    }
}
