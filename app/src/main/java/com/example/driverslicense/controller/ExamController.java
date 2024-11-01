package com.example.driverslicense.controller;


import android.util.Log;

import com.example.driverslicense.model.exam.Exam;
import com.example.driverslicense.model.exam.QuestionExam;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// ... các import khác

public class ExamController implements JsonDeserializer<Exam> {
    private static final String TAG = "ExamDeserializer"; // Thêm một hằng số TAG để sử dụng cho log

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
            // Log các câu hỏi
            for (QuestionExam question : questions) {
                Log.d(TAG, "Question: " + question.toString()); // Bạn có thể cần override phương thức toString() trong QuestionExam
            }
        } else if (questionsElement.isJsonPrimitive()) {
            String questionsString = questionsElement.getAsString();
            Log.d(TAG, "Questions as string: " + questionsString);
        }

        return new Exam(exam_type, id, is_fixed, questions, set_fixed_number, user_id);
    }
}

