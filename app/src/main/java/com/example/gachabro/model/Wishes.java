package com.example.gachabro.model;

public class Wishes {

    String total_char;
    String  five_char;
    String  four_char;

    String  total_weapon;
    String five_weapon;
    String four_weapon;

    String total_standard;
    String five_standard;
    String four_standard;

    public Wishes(){}

    public Wishes(String total_char, String five_char, String four_char, String total_weapon, String five_weapon, String four_weapon, String total_standard, String five_standard, String four_standard) {
        this.total_char = total_char;
        this.five_char = five_char;
        this.four_char = four_char;
        this.total_weapon = total_weapon;
        this.five_weapon = five_weapon;
        this.four_weapon = four_weapon;
        this.total_standard = total_standard;
        this.five_standard = five_standard;
        this.four_standard = four_standard;
    }

    public String getTotal_char() {
        return total_char;
    }

    public void setTotal_char(String total_char) {
        this.total_char = total_char;
    }

    public String getFive_char() {
        return five_char;
    }

    public void setFive_char(String five_char) {
        this.five_char = five_char;
    }

    public String getFour_char() {
        return four_char;
    }

    public void setFour_char(String four_char) {
        this.four_char = four_char;
    }

    public String getTotal_weapon() {
        return total_weapon;
    }

    public void setTotal_weapon(String total_weapon) {
        this.total_weapon = total_weapon;
    }

    public String getFive_weapon() {
        return five_weapon;
    }

    public void setFive_weapon(String five_weapon) {
        this.five_weapon = five_weapon;
    }

    public String getFour_weapon() {
        return four_weapon;
    }

    public void setFour_weapon(String four_weapon) {
        this.four_weapon = four_weapon;
    }

    public String getTotal_standard() {
        return total_standard;
    }

    public void setTotal_standard(String total_standard) {
        this.total_standard = total_standard;
    }

    public String getFive_standard() {
        return five_standard;
    }

    public void setFive_standard(String five_standard) {
        this.five_standard = five_standard;
    }

    public String getFour_standard() {
        return four_standard;
    }

    public void setFour_standard(String four_standard) {
        this.four_standard = four_standard;
    }
}
