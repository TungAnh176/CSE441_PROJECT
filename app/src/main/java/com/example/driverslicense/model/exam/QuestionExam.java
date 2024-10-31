package com.example.driverslicense.model.exam;

public class QuestionExam {
    private int question_id;

    public QuestionExam(int question_id) {
        this.question_id = question_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    @Override
    public String toString() {
        return "QuestionExam{" +
                "question_id=" + question_id +
                '}';
    }
}
