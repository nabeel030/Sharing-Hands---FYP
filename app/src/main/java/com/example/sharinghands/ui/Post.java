package com.example.sharinghands.ui;

public class Post {
    private int src_image_logo;
    private String ngo_title;
    private String post_title;
    private String post_details;
    private int src_img_post;
    private int raised_amount;
    private int required_amount;

    public Post() {
    }

    public Post(int src_image_logo, String ngo_title, String post_title, String post_details, int src_img_post, int raised_amount, int remaining_amount) {
        this.src_image_logo = src_image_logo;
        this.ngo_title = ngo_title;
        this.post_title = post_title;
        this.post_details = post_details;
        this.src_img_post = src_img_post;
        this.raised_amount = raised_amount;
        this.required_amount = remaining_amount;
    }

    public int getSrc_image_logo() {
        return src_image_logo;
    }

    public void setSrc_image_logo(int src_image_logo) {
        this.src_image_logo = src_image_logo;
    }

    public String getNgo_title() {
        return ngo_title;
    }

    public void setNgo_title(String ngo_title) {
        this.ngo_title = ngo_title;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_details() {
        return post_details;
    }

    public void setPost_details(String post_details) {
        this.post_details = post_details;
    }

    public int getSrc_img_post() {
        return src_img_post;
    }

    public void setSrc_img_post(int src_img_post) {
        this.src_img_post = src_img_post;
    }

    public int getRaised_amount() {
        return raised_amount;
    }

    public void setRaised_amount(int raised_amount) {
        this.raised_amount = raised_amount;
    }

    public int getRequired_amount() {
        return required_amount;
    }

    public void setRequired_amount(int required_amount) {
        this.required_amount = required_amount;
    }
}


