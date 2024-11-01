package com.example.driverslicense.view.exam;

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

import com.example.driverslicense.controller.QuestionController;
import com.example.driverslicense.model.exam.QuestionSave;
import com.example.driverslicense.model.question.AnswerQuestionHistory;
import com.example.driverslicense.model.question.Answers;
import com.example.driverslicense.model.question.Option;
import com.example.driverslicense.model.question.Question;
import com.example.driverslicense.view.content.DetailQuesionActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class DetailQuestionExamActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "QuestionPrefs";
    private static final String SELECTED_ANSWER_KEY = "selected_answer";


    TextView textView, txtDescription;
    ApiServices apiInterface;
    Question question;
    LinearLayout optionsContainer;
    private String selectedAnswer;
    private int questionId;
    ImageView imgBack;
    ImageView imgHinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_quesion);

        initViews();
        setupBackButton();

        questionId = getIntent().getIntExtra("id", 1);
        fetchQuestion(questionId);
    }

    private void initViews() {
        textView = findViewById(R.id.txtNameQuesion);
        imgHinh = findViewById(R.id.imgHinh);
        optionsContainer = findViewById(R.id.llOption);
        txtDescription = findViewById(R.id.txtDescription);
        imgBack = findViewById(R.id.imgBack);
    }

    private void setupBackButton() {
        imgBack.setOnClickListener(v -> finish());
    }

    private void fetchQuestion(int questionId) {
        Gson gson = buildGson();
        Retrofit retrofit = buildRetrofit(gson);

        apiInterface = retrofit.create(ApiServices.class);
        apiInterface.getDetailQuestion(questionId).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful() && response.body() != null) {
                    question = response.body();
                    displayQuestionContent();
                    displayOptions();
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

    private Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Question.class, new QuestionController())
                .create();
    }

    private Retrofit buildRetrofit(Gson gson) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    private void displayQuestionContent() {
        textView.setText(question.getContent());
        selectedAnswer = getSavedAnswerForQuestion(questionId);
    }

    private void displayOptions() {
        optionsContainer.removeAllViews();
        List<Option> options = question.getOptions();

        for (Option option : options) {
            if (option.getImage() != null) {
                loadOptionImage(option.getImage());
            } else {
                imgHinh.setVisibility(View.GONE);
            }

            createOptionButtons(option);
        }
    }

    private void loadOptionImage(String imageUrl) {
        imgHinh.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .into(imgHinh);
    }

    private void createOptionButtons(Option option) {
        if (option.getA() != null) createOptionButton("a: " + option.getA(), "a");
        if (option.getB() != null) createOptionButton("b: " + option.getB(), "b");
        if (option.getC() != null) createOptionButton("c: " + option.getC(), "c");
        if (option.getD() != null) createOptionButton("d: " + option.getD(), "d");
    }

    private String getSavedAnswerForQuestion(int questionId) {
        for (QuestionSave answer : DataMemory.DATA_SAVE_QUESTION.getAnswers()) {
            if (answer.getQuestion_id() == questionId) {
                return answer.getUser_answer();
            }
        }
        return null;
    }

    private void createOptionButton(String optionText, String optionLabel) {
        Button button = new Button(DetailQuestionExamActivity.this);
        button.setText(optionText);

        styleOptionButton(button, optionLabel);
        button.setOnClickListener(v -> onOptionSelected(button, optionLabel));

        optionsContainer.addView(button);
    }

    private void styleOptionButton(Button button, String optionLabel) {
        if (optionLabel.equals(selectedAnswer)) {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        } else {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 16, 0, 16);
        button.setLayoutParams(layoutParams);
    }

    private void onOptionSelected(Button button, String optionLabel) {
        resetOptionButtonColors();
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.green));

        selectedAnswer = optionLabel;
        saveAnswerForQuestion(questionId, optionLabel);
    }

    private void resetOptionButtonColors() {
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            View child = optionsContainer.getChildAt(i);
            if (child instanceof Button) {
                child.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            }
        }
    }

    private void saveAnswerForQuestion(int questionId, String answer) {
        DataMemory.DATA_SAVE_QUESTION.getAnswers().removeIf(q -> q.getQuestion_id() == questionId);
        DataMemory.DATA_SAVE_QUESTION.getAnswers().add(new QuestionSave(questionId, answer));
    }}
