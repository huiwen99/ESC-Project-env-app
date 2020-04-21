package com.example.env;

public class UserForFirebase {
    String UID;
    String teleID;
    String email;
    Boolean adminRights;

    public UserForFirebase(String UID, String teleID, String email, Boolean adminRights) {
        this.UID = UID;
        this.teleID = teleID;
        this.adminRights = adminRights;
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public String getTeleID() {
        return teleID;
    }

    public Boolean getAdminRights() {
        return adminRights;
    }

    public String getEmail() {
        return email;
    }
}
