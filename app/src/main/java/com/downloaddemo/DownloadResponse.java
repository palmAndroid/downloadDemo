package com.downloaddemo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DownloadResponse {

    @SerializedName("data")
    private List<Download> results;

    @SerializedName("download_time")
    private String downloadTime;

    private String errorMessage = "";
    private boolean isError;

    public List<Download> getResults() {
        return results;
    }

    public void setResults(List<Download> results) {
        this.results = results;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

}
