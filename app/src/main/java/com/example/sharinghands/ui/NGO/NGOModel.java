package com.example.sharinghands.ui.NGO;

public class NGOModel {
    private String ngo_title;
    private String email;
    private String address;
    private int reg_number;


    public NGOModel(){

    }

    public NGOModel(String ngo_title, String email, String address, int reg_number) {
        this.ngo_title = ngo_title;
        this.email = email;
        this.address = address;
        this.reg_number = reg_number;
    }

    public String getNgo_title() {
        return ngo_title;
    }

    public void setNgo_title(String ngo_title) {
        this.ngo_title = ngo_title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getReg_number() {
        return reg_number;
    }

    public void setReg_number(int reg_number) {
        this.reg_number = reg_number;
    }
}
