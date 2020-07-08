package com.example.sessions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportModel {

    @SerializedName("reportId")
    @Expose
    private int reportId;

    @SerializedName("reporter")
    @Expose
    private String reporter;

    @SerializedName("spotId")
    @Expose
    private int spotId;
    @SerializedName("reportedUser")
    @Expose
    private String reportedUser;
    @SerializedName("reason")
    @Expose
    private String reason;


    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }


    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }



    public String getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(String reportedUser) {
        this.reportedUser = reportedUser;
    }



    public String  getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
