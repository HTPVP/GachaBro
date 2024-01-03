package com.example.gachabro.model;

public class Character_items {


//    private String name;
//    private String image;
    private String imagePath;
    private String prefix;

    public Character_items(String imagePath, String prefix) {
        this.prefix = prefix;
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}