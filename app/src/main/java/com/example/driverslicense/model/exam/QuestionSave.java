package com.example.driverslicense.model.exam;

public class QuestionSave {
    private int question_id;
    private String user_answer;

    public QuestionSave() {
    }

    public QuestionSave(int question_id, String user_answer) {
        this.question_id = question_id;
        this.user_answer = user_answer;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }
}
