package Models;

public class Comment {

    private String comment;
    private String senderId;

    public Comment() {
    }

    public Comment(String comment, String senderId) {
        this.comment = comment;
        this.senderId = senderId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
