package com.example.driverslicense.view.content;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.driverslicense.DataLocal.DataMemory;
import com.example.driverslicense.R;
import com.example.driverslicense.api.ApiServices;
import com.example.driverslicense.model.question.AnswerQuestionHistory;
import com.example.driverslicense.model.question.Answers;
import com.example.driverslicense.model.question.Option;
import com.example.driverslicense.model.question.Question;
import com.example.driverslicense.model.exam.QuestionSave;
import com.example.driverslicense.controller.QuestionController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailQuesionActivity extends AppCompatActivity {

    TextView textView, txtDescription, txtCorrect;
    ApiServices apiInterface;
    Question question;
    LinearLayout optionsContainer;
    private String selectedAnswer;
    private int questionId;
    ImageView imgBack;
    ImageView imgHinh;
    private List<Button> optionButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_quesion);

        textView = findViewById(R.id.txtNameQuesion);
        imgHinh = findViewById(R.id.imgHinh);
        optionsContainer = findViewById(R.id.llOption);
        txtDescription = findViewById(R.id.txtDescription);
        questionId = getIntent().getIntExtra("id", 1);
        imgBack = findViewById(R.id.imgBack);
        txtCorrect = findViewById(R.id.txt_correct);
        imgBack.setOnClickListener(v -> {
            finish();
        });
        fectchQuestion(questionId);
    }

    private void fectchQuestion(int questionId) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Question.class, new QuestionController())
                .create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/") // Sử dụng địa chỉ localhost cho Android Emulator
                .addConverterFactory(GsonConverterFactory.create(gson)).client(client)
                .build();

        apiInterface = retrofit.create(ApiServices.class);
        apiInterface.getDetailQuestion(questionId).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful() && response.body() != null) {
                    question = response.body();
                    textView.setText(question.getContent());

                    List<Option> options = question.getOptions();
                    optionsContainer.removeAllViews();

                    for (Option option : options) {
                        if (option.getImage() != null) {
                            imgHinh.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext())
                                    .load(option.getImage())
                                    .into(imgHinh);
                        } else {
                            imgHinh.setVisibility(View.GONE);
                        }
                        if (option.getA() != null) createOptionButton("a: " + option.getA(), "a");
                        if (option.getB() != null) createOptionButton("b: " + option.getB(), "b");
                        if (option.getC() != null) createOptionButton("c: " + option.getC(), "c");
                        if (option.getD() != null) createOptionButton("d: " + option.getD(), "d");
                    }
                } else {
                    Log.e("DetailQuesionActivity", "Response unsuccessful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable throwable) {
                Log.e("DetailQuesionActivity", "Failure: " + throwable.getMessage());
            }
        });
    }

    // Phương thức tạo nút cho từng option
    private void createOptionButton(String optionText, String optionLabel) {
        if (optionText != null) {
            Button button = new Button(DetailQuesionActivity.this);
            button.setText(optionText);

            // Đặt màu nền mặc định cho nút
            button.setBackgroundColor(ContextCompat.getColor(DetailQuesionActivity.this, R.color.gray));

            // Tạo LayoutParams để thêm khoảng cách (margin)
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // Chiều rộng
                    LinearLayout.LayoutParams.WRAP_CONTENT  // Chiều cao
            );
            layoutParams.setMargins(0, 16, 0, 16); // Thiết lập khoảng cách: trên, dưới

            // Áp dụng LayoutParams cho nút
            button.setLayoutParams(layoutParams);

            button.setOnClickListener(v -> {
                DataMemory.DATA_SAVE_QUESTION.getAnswers().add(new QuestionSave(getIntent().getIntExtra("id", 1), optionLabel));
                selectedAnswer = optionLabel;
                String lowerCaseLabel = optionLabel.toLowerCase();
                sendAnswer(selectedAnswer, button);
            });

            // Thêm nút vào container
            optionsContainer.addView(button);
            optionButtons.add(button); // Lưu nút vào danh sách
        }
    }

    private void resetAllButtonsColor() {
        for (Button btn : optionButtons) {
            btn.setBackgroundColor(ContextCompat.getColor(DetailQuesionActivity.this, R.color.gray));
        }
    }

    private void sendAnswer(String answer, Button button) {
        // Kiểm tra giá trị của questionId và answer
        resetAllButtonsColor();
        Log.e("Question ID", String.valueOf(questionId));
        Log.e("Selected Answer", answer);

        // Tạo Map để lưu câu trả lời
        Map<String, String> answerMap = new HashMap<>();
        answerMap.put(String.valueOf(questionId), answer); // Gán ID và câu trả lời

        // Tạo đối tượng Answers và gán Map vào nó
        Answers request = new Answers();
        request.setAnswers(answerMap); // Gán answerMap vào answers

        // Chuyển đổi đối tượng request thành chuỗi JSON
        String jsonRequest = new Gson().toJson(request);
        Log.e("CheckAnswerRequest", jsonRequest); // Log chuỗi JSON

        // Gọi API
        apiInterface.checkAnswer(request).enqueue(new Callback<AnswerQuestionHistory>() {
            @Override
            public void onResponse(Call<AnswerQuestionHistory> call, Response<AnswerQuestionHistory> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AnswerQuestionHistory checkAnswerResponse = response.body();

                        // Kiểm tra giá trị của answers
                        Map<String, Boolean> answers = checkAnswerResponse.getAnswers();
                        if (answers != null && answers.containsKey(String.valueOf(questionId))) {
                            boolean isCorrect = answers.get(String.valueOf(questionId));

                            // Hiển thị thông báo dựa trên giá trị isCorrect
                            if (isCorrect) {
                                Log.d("TAG", "Bạn chọn đúng.");
                                // Bạn có thể hiện thông báo trên UI
                                Toast.makeText(DetailQuesionActivity.this, "Bạn chọn đúng.", Toast.LENGTH_SHORT).show();
                                txtDescription.setVisibility(View.GONE);

                                button.setBackgroundColor(ContextCompat.getColor(DetailQuesionActivity.this, R.color.green));
                            } else {
                                Log.d("TAG", "Bạn chọn sai.");
                                // Bạn có thể hiện thông báo trên UI
                                Toast.makeText(DetailQuesionActivity.this, "Bạn chọn sai.", Toast.LENGTH_SHORT).show();
                                String text = question.getOptions().get(0).getDescription();
                                String correctAnswer = question.getCorrect_answer();
                                txtDescription.setText(text);
                                txtCorrect.setText("Đáp án đúng: " + correctAnswer.toUpperCase());
                                txtCorrect.setVisibility(View.VISIBLE);
                                txtDescription.setVisibility(View.VISIBLE);
                                button.setBackgroundColor(ContextCompat.getColor(DetailQuesionActivity.this, R.color.red));
                                txtCorrect.setBackgroundColor(ContextCompat.getColor(DetailQuesionActivity.this, R.color.green));

                            }
                        } else {
                            Log.e("TAG", "Không tìm thấy câu trả lời cho câu hỏi.");
                        }

                        String jsonResponse = new Gson().toJson(checkAnswerResponse);
                        Log.e("CheckAnswerResponse Body", jsonResponse);
                    } else {
                        Log.e("TAG", "Response body is null");
                    }
                } else {
                    Log.e("DetailQuesionActivity", "Response unsuccessful: " + response.message());
                    Log.e("DetailQuesionActivity", "Error Body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<AnswerQuestionHistory> call, Throwable throwable) {
                Log.e("DetailQuesionActivity", "Failure: " + throwable.getMessage());
            }
        });

    }

    private void CreateTextView(String text) {
        if (text != null) {
            txtDescription.setText(text);
        }
    }


}
