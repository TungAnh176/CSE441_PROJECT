package com.example.driverslicense.model.question;

import java.util.List;

public class Question {
    private int id;
    private String content;
    private List<Option> options;
    private String correct_answer;
    private int category;

    public Question(int id, String content, List<Option> options, String correct_answer, int category) {
        this.id = id;
        this.content = content;
        this.options = options;
        this.correct_answer = correct_answer;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
