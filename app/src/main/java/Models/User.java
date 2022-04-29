package Models;

public class User {

    private String UID;
    private String FullName;
    private String Username;
    private String Bio;
    private String type;
    private String image;

    public User(){

    }

    public User(String UID, String fullName, String username, String bio, String type, String image) {
        this.UID = UID;
        this.FullName = fullName;
        this.Username = username;
        this.Bio = bio;
        this.type = type;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getFullName() {
        return this.FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUsername() {
        return this.Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
