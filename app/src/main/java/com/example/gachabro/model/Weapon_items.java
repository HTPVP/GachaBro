package com.example.gachabro.model;

public class Weapon_items {

    private String imagePath;
    private String prefix;

    public Weapon_items(String imagePath, String prefix) {
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