package com.example.driverslicense.model.exam;

import java.util.ArrayList;
import java.util.List;

public class SaveAnswer {
    private String user_id ="";
    private String exam_id = "";
    private List<QuestionSave> answers = new ArrayList<>();

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public List<QuestionSave> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionSave> answers) {
        this.answers = answers;
    }
}
