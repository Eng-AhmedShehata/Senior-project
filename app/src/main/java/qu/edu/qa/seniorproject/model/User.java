package qu.edu.qa.seniorproject.model;

import android.net.Uri;

import java.io.Serializable;

public class User implements Serializable {
    String name;
    String id;
    String type;
    String email;
    String profileImage;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public User(String name, String id, String type, String email) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.email = email;
    }
    public User(String name, String id, String type, String email,String pro) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.email = email;
        this.profileImage=pro;
    }

    public User() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
