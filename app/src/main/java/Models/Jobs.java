package Models;

public class Jobs {

    String JobTitle;
    String JobDes;
    String Publisher;
    String postID;
    String userId;
    String location;
    String Cimage;

    public Jobs(){

    }

    public Jobs(String jobTitle, String jobDes, String publisher, String postID, String userId, String location, String cimage) {
        JobTitle = jobTitle;
        JobDes = jobDes;
        Publisher = publisher;
        this.postID = postID;
        this.userId = userId;
        this.location = location;
        Cimage = cimage;
    }

    public String getCimage() {
        return Cimage;
    }

    public void setCimage(String cimage) {
        Cimage = cimage;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getJobDes() {
        return JobDes;
    }

    public void setJobDes(String jobDes) {
        JobDes = jobDes;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
