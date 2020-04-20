package qu.edu.qa.seniorproject.model;


import java.io.Serializable;

public class Place implements Serializable {
    private String photo;
    private String title;
    private String text;
    private String location;
    private String building;

    public Place() {
    }

    public Place(String photo, String title, String text, String location, String building) {
        this.photo = photo;
        this.title = title;
        this.text = text;
        this.location = location;
        this.building = building;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
