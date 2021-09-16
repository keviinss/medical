package com.example.medical.model;

public class User {
    private String id;
    private String namaLengkap;
    private String email;
    private String imageURL;
    private String status;
    private String password;

    public User(String id, String namaLengkap, String email, String imageURL, String status, String password){
        this.id = id;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.imageURL = imageURL;
        this.status = status;
        this.password = password;
    }

    public User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
