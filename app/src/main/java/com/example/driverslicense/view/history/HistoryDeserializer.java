package com.example.driverslicense.view.history;

import com.example.driverslicense.model.History;
import com.example.driverslicense.model.QuestionHistory;
import com.example.driverslicense.model.AnswerHistory;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryDeserializer implements JsonDeserializer<History> {

    @Override
    public History deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int id = json.getAsJsonObject().get("id").getAsInt();
        int exam_id = json.getAsJsonObject().get("exam_id").getAsInt();
        int user_id = json.getAsJsonObject().get("user_id").getAsInt();
        int score = json.getAsJsonObject().get("score").getAsInt();
        boolean pass = json.getAsJsonObject().get("pass").getAsBoolean();

        // Deserialize questions
        JsonElement questionsElement = json.getAsJsonObject().get("questions");
        List<QuestionHistory> questions = new ArrayList<>();

        if (questionsElement.isJsonArray()) {
            JsonArray questionsArray = questionsElement.getAsJsonArray();
            for (JsonElement questionElement : questionsArray) {
                QuestionHistory question = context.deserialize(questionElement, QuestionHistory.class);
                questions.add(question);
            }
        }

        // Deserialize answers
        JsonElement answersElement = json.getAsJsonObject().get("answers");
        List<AnswerHistory> answers = new ArrayList<>();

        if (answersElement.isJsonArray()) {
            JsonArray answersArray = answersElement.getAsJsonArray();
            for (JsonElement answerElement : answersArray) {
                AnswerHistory answer = context.deserialize(answerElement, AnswerHistory.class);
                answers.add(answer);
            }
        }

        return new History(id, exam_id, user_id, questions, answers, score, pass);
    }
}
