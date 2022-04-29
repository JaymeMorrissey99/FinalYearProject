package Models;

public class confirmedJobs {

    private String companyId;
    private String date;
    private String jobId;
    private String mFullName;
    private String modelId;
    private String muName;
    private String publisher;
    private String status;

    public confirmedJobs() {
    }

    public confirmedJobs(String companyId, String date, String jobId, String mFullName, String modelId, String muName, String publisher, String status) {
        this.companyId = companyId;
        this.date = date;
        this.jobId = jobId;
        this.mFullName = mFullName;
        this.modelId = modelId;
        this.muName = muName;
        this.publisher = publisher;
        this.status = status;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getmFullName() {
        return mFullName;
    }

    public void setmFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getMuName() {
        return muName;
    }

    public void setMuName(String muName) {
        this.muName = muName;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
