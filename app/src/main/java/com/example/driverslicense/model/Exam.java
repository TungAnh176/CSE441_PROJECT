package com.example.driverslicense.model;

import java.util.List;

public class Exam {
    private int id;
    private int user_id;
    private List<QuestionExam> questions;
    private int exam_type;
    private int is_fixed;
    private int set_fixed_number;

    public Exam(int exam_type, int id, int is_fixed, List<QuestionExam> questions, int set_fixed_number, int user_id) {
        this.exam_type = exam_type;
        this.id = id;
        this.is_fixed = is_fixed;
        this.questions = questions;
        this.set_fixed_number = set_fixed_number;
        this.user_id = user_id;
    }

    public int getExam_type() {
        return exam_type;
    }

    public void setExam_type(int exam_type) {
        this.exam_type = exam_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_fixed() {
        return is_fixed;
    }

    public void setIs_fixed(int is_fixed) {
        this.is_fixed = is_fixed;
    }

    public List<QuestionExam> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionExam> questions) {
        this.questions = questions;
    }

    public int getSet_fixed_number() {
        return set_fixed_number;
    }

    public void setSet_fixed_number(int set_fixed_number) {
        this.set_fixed_number = set_fixed_number;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
