package com.example.driverslicense.controller;

import com.example.driverslicense.model.history.AnswerHistory;
import com.example.driverslicense.model.history.History;
import com.example.driverslicense.model.history.QuestionHistory;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryController implements JsonDeserializer<History> {

    @Override
    public History deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int id = json.getAsJsonObject().get("id").getAsInt();
        int exam_id = json.getAsJsonObject().get("exam_id").getAsInt();
        int user_id = json.getAsJsonObject().get("user_id").getAsInt();
        int score = json.getAsJsonObject().get("score").getAsInt();
        boolean pass = json.getAsJsonObject().get("pass").getAsBoolean();

        // Deserialize questions as List<QuestionHistory>
        JsonElement questionsElement = json.getAsJsonObject().get("questions");
        List<QuestionHistory> questions = new ArrayList<>();

        if (questionsElement.isJsonArray()) {
            JsonArray questionsArray = questionsElement.getAsJsonArray();
            for (JsonElement questionElement : questionsArray) {
                int questionId = questionElement.getAsInt();
                QuestionHistory question = new QuestionHistory(questionId); // Assuming this constructor exists
                questions.add(question);
            }
        }

        // Deserialize answers as List<AnswerHistory>
        JsonElement answersElement = json.getAsJsonObject().get("answers");
        List<AnswerHistory> answers = new ArrayList<>();

        if (answersElement.isJsonArray()) {
            JsonArray answersArray = answersElement.getAsJsonArray();
            for (JsonElement answerElement : answersArray) {
                String answer = answerElement.getAsString();
                answers.add(new AnswerHistory(answer));
            }
        }

        return new History(id, exam_id, user_id, questions, answers, score, pass);
    }
}
