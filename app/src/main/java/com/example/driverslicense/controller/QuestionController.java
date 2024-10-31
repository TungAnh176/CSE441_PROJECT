package com.example.driverslicense.controller;

import android.util.Log;

import com.example.driverslicense.model.question.Option;
import com.example.driverslicense.model.question.Question;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class QuestionController implements JsonDeserializer<Question> {
    @Override
    public Question deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int id = jsonObject.has("id") && !jsonObject.get("id").isJsonNull() ? jsonObject.get("id").getAsInt() : 0;
        String content = jsonObject.has("content") && !jsonObject.get("content").isJsonNull() ? jsonObject.get("content").getAsString() : "";
        String correctAnswer = jsonObject.has("correct_answer") && !jsonObject.get("correct_answer").isJsonNull() ? jsonObject.get("correct_answer").getAsString() : "";
        int category = jsonObject.has("category") && !jsonObject.get("category").isJsonNull() ? jsonObject.get("category").getAsInt() : 0;

        Log.i("Question", "ID: " + id);
        Log.i("Question", "Content: " + content);
        Log.i("Question", "Correct Answer: " + correctAnswer);
        Log.i("Question", "Category: " + category);

        // Lấy phần options từ JSON
        JsonElement optionsElement = jsonObject.get("options");
        List<Option> options = new ArrayList<>();

        if (optionsElement != null && optionsElement.isJsonObject()) {
            JsonObject optionsObject = optionsElement.getAsJsonObject().getAsJsonObject("options");

            String a = optionsObject.has("a") && !optionsObject.get("a").isJsonNull() ? optionsObject.get("a").getAsString() : null;
            String b = optionsObject.has("b") && !optionsObject.get("b").isJsonNull() ? optionsObject.get("b").getAsString() : null;
            String c = optionsObject.has("c") && !optionsObject.get("c").isJsonNull() ? optionsObject.get("c").getAsString() : null;
            String d = optionsObject.has("d") && !optionsObject.get("d").isJsonNull() ? optionsObject.get("d").getAsString() : null;

            // Lấy thông tin về hình ảnh và mô tả
            String image = optionsElement.getAsJsonObject().getAsJsonObject("image").has("img") &&
                    !optionsElement.getAsJsonObject().getAsJsonObject("image").get("img").isJsonNull() ?
                    optionsElement.getAsJsonObject().getAsJsonObject("image").get("img").getAsString() : null;

            String description = optionsElement.getAsJsonObject().getAsJsonObject("description").has("description") &&
                    !optionsElement.getAsJsonObject().getAsJsonObject("description").get("description").isJsonNull() ?
                    optionsElement.getAsJsonObject().getAsJsonObject("description").get("description").getAsString() : null;

            // Tạo một đối tượng Option với các tùy chọn
            Option option = new Option(a, b, c, d, image, description);
            options.add(option);
            Log.i("Question", "Option: " + option.toString());
        }

        // Tạo đối tượng Question với danh sách tùy chọn
        Question question = new Question(id, content, options, correctAnswer, category);
        Log.i("Question", "Deserialized Question: " + question.toString());
        return question;
    }
}
