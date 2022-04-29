package Models;

public class JobApplication {

    String applicationID;
    String companyId;
    String jobDes;
    String jobId;
    String jobTitle;
    String request_type;
    String senderId;
    String publisher;
    String mnane;
    String muname;

    public JobApplication() {
    }

    public JobApplication(String applicationID, String companyId, String jobDes, String jobId, String jobTitle, String request_type, String senderId, String publisher, String mnane, String muname) {
        this.applicationID = applicationID;
        this.companyId = companyId;
        this.jobDes = jobDes;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.request_type = request_type;
        this.senderId = senderId;
        this.publisher = publisher;
        this.mnane = mnane;
        this.muname = muname;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getJobDes() {
        return jobDes;
    }

    public void setJobDes(String jobDes) {
        this.jobDes = jobDes;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getMnane() {
        return mnane;
    }

    public void setMnane(String mnane) {
        this.mnane = mnane;
    }

    public String getMuname() {
        return muname;
    }

    public void setMuname(String muname) {
        this.muname = muname;
    }
}
