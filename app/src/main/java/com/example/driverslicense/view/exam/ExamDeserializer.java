package com.example.driverslicense.view.exam;

import com.example.driverslicense.model.Exam;
import com.example.driverslicense.model.QuestionExam;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExamDeserializer implements JsonDeserializer<Exam> {
    @Override
    public Exam deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int id = json.getAsJsonObject().get("id").getAsInt();
        int user_id = json.getAsJsonObject().get("user_id").getAsInt();
        int exam_type = json.getAsJsonObject().get("exam_type").getAsInt();
        int is_fixed = json.getAsJsonObject().get("is_fixed").getAsInt();
        int set_fixed_number = json.getAsJsonObject().get("set_fixed_number").getAsInt();

        // Kiểm tra xem "questions" là chuỗi hay mảng
        JsonElement questionsElement = json.getAsJsonObject().get("questions");
        List<QuestionExam> questions = new ArrayList<>();

        if (questionsElement.isJsonArray()) {
            JsonArray questionsArray = questionsElement.getAsJsonArray();
            for (JsonElement questionElement : questionsArray) {
                QuestionExam question = context.deserialize(questionElement, QuestionExam.class);
                questions.add(question);
            }
        } else if (questionsElement.isJsonPrimitive()) {
            String questionsString = questionsElement.getAsString();
        }

        return new Exam(exam_type, id, is_fixed, questions, set_fixed_number, user_id);
    }
}
