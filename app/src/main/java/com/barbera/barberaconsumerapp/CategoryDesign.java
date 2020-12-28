package com.barbera.barberaconsumerapp;

public class CategoryDesign {
    private String CategoryImage;
    private String CategoryName;
    private String[] services;

    public CategoryDesign(String categoryImage, String categoryName) {
        CategoryImage = categoryImage;
        CategoryName = categoryName;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public String getCategoryName() {
        return CategoryName;
    }
}
