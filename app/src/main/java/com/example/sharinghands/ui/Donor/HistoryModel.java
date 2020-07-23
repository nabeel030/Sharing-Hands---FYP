package com.example.sharinghands.ui.Donor;

public class HistoryModel {
    private String NGO_name;
    private String PostTitle;
    private int AmountDonated;

    public HistoryModel() {

    }

    public HistoryModel(String NGO_name, String postTitle, int amountDonated) {
        this.NGO_name = NGO_name;
        this.PostTitle = postTitle;
        this.AmountDonated = amountDonated;
    }

    public String getNGO_name() {
        return NGO_name;
    }

    public void setNGO_name(String NGO_name) {
        this.NGO_name = NGO_name;
    }

    public String getPostTitle() {
        return PostTitle;
    }

    public void setPostTitle(String postTitle) {
        PostTitle = postTitle;
    }

    public int getAmountDonated() {
        return AmountDonated;
    }

    public void setAmountDonated(int amountDonated) {
        AmountDonated = amountDonated;
    }
}
