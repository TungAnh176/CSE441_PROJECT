package com.example.driverslicense.model.question;

import java.util.Map;

public class AnswerQuestionHistory {
    private Map<String, Boolean> answers;// Thêm trường mô tả

    // Getter và Setter cho các trường
    public Map<String, Boolean> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Boolean> answers) {
        this.answers = answers;
    }
}
