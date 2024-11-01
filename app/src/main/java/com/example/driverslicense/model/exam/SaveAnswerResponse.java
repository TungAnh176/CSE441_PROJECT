package com.example.driverslicense.model.exam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveAnswerResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("pass")
    @Expose
    private Boolean pass;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

}
