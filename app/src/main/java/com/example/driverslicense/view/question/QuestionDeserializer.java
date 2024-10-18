package com.example.driverslicense.view.question;

import com.example.driverslicense.model.Option;
import com.example.driverslicense.model.Question;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionDeserializer implements JsonDeserializer<Question> {
    @Override
    public Question deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int id = json.getAsJsonObject().get("id").getAsInt();
        String content = json.getAsJsonObject().get("content").getAsString();
        String correct_answer = json.getAsJsonObject().get("correct_answer").getAsString();
        int category = json.getAsJsonObject().get("category").getAsInt();

        // Kiểm tra xem "options" là chuỗi hay mảng
        JsonElement optionsElement = json.getAsJsonObject().get("options");
        List<Option> options = new ArrayList<>();

        if (optionsElement.isJsonArray()) {
            // Nếu "options" là mảng, deserialize từng phần tử
            JsonArray optionsArray = optionsElement.getAsJsonArray();
            for (JsonElement optionElement : optionsArray) {
                Option option = context.deserialize(optionElement, Option.class);
                options.add(option);
            }
        } else if (optionsElement.isJsonPrimitive()) {
            // Nếu "options" là chuỗi hoặc giá trị đơn, bạn có thể xử lý riêng (tuỳ yêu cầu của bạn)
            String optionsString = optionsElement.getAsString();
            // Có thể xử lý string này tuỳ theo cấu trúc hoặc thêm log để kiểm tra
            System.out.println("Options is a string: " + optionsString);
        }

        return new Question(id, content, options, correct_answer, category);
    }
}
