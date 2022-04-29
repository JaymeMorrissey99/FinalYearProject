package Models;

public class Post {

    private  String postid ;
    private  String postimage ;
    private  String description ;
    private  String username ;
    private  String uID;
    private String profileImg;

    public Post() {
    }

    public Post(String postid, String postimage, String description, String username, String uID, String profileImg) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.username = username;
        this.uID = uID;
        this.profileImg = profileImg;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
