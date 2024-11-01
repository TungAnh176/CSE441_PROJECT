package com.example.driverslicense.model.history;

import com.example.driverslicense.model.exam.Exam;

import java.util.List;

public class History {
    private int id;
    private int exam_id;
    private int user_id;
    private List<QuestionHistory> questions;
    private List<AnswerHistory> answers;
    private int score;
    private Boolean pass;

    public History(int id, int exam_id, int user_id, List<QuestionHistory> questions, List<AnswerHistory> answers, int score, Boolean pass) {
        this.id = id;
        this.exam_id = exam_id;
        this.user_id = user_id;
        this.questions = questions;
        this.answers = answers;
        this.score = score;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExam_id() {
        return exam_id;
    }

    public void setExam_id(int exam_id) {
        this.exam_id = exam_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<QuestionHistory> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionHistory> questions) {
        this.questions = questions;
    }

    public List<AnswerHistory> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerHistory> answers) {
        this.answers = answers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Boolean getPass() {
        return pass;
    }


}
