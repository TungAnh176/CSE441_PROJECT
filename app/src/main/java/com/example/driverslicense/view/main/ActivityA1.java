package com.example.driverslicense.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.driverslicense.R;
import com.example.driverslicense.api.ApiServices;
import com.example.driverslicense.model.exam.Exam;
import com.example.driverslicense.model.exam.RandomResponse;
import com.example.driverslicense.view.content.ContentActivity;
import com.example.driverslicense.view.exam.ExamActivity;
import com.example.driverslicense.controller.ExamController;
import com.example.driverslicense.view.history.HistoryActivity;
import com.example.driverslicense.view.question.QuestionActivity;
import com.example.driverslicense.view.random.RandomExamActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityA1 extends AppCompatActivity {
    Button btnBack, btnRandom, btnExam, btnList, btnContent, btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBack = findViewById(R.id.btn_back);
        btnRandom = findViewById(R.id.btn_random);
        btnExam = findViewById(R.id.btn_exam);
        btnList = findViewById(R.id.btn_list);
        btnContent = findViewById(R.id.btn_content);
        btnHistory = findViewById(R.id.btn_history);
        //Quay lại giao diện chính
        setupBackButton();
        //Chuyển đến giao diện exam
        setupExamButton();
        //Chuyển đến giao diện lịch sử
        setupHistoryButton();

        setBtnList();

        setupContentButton();

        btnRandom.setOnClickListener(v -> {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Exam.class, new ExamController())
                    .create();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8000/api/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            ApiServices apiServices = retrofit.create(ApiServices.class);
            apiServices.getRandom(1).enqueue(new Callback<RandomResponse>() {
                @Override
                public void onResponse(Call<RandomResponse> call, Response<RandomResponse> response) {
                    startActivity(new Intent(ActivityA1.this, RandomExamActivity.class)
                            .putExtra("exam_id", response.body().getExam_id()));
                }

                @Override
                public void onFailure(Call<RandomResponse> call, Throwable throwable) {

                }
            });

        });
    }

    private void setupHistoryButton() {
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityA1.this, HistoryActivity.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void setupExamButton() {
        btnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityA1.this, ExamActivity.class);
                intent.putExtra("exam_name", "Bộ đề A1");
                intent.putExtra("exam_id", 1);
                startActivity(intent);
            }
        });
    }

    private void setBtnList() {
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityA1.this, QuestionActivity.class);
                intent.putExtra("name", "200 câu");
                intent.putExtra("type_id", 1);
                startActivity(intent);
            }
        });
    }

    private void setupContentButton() {
        btnContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityA1.this, ContentActivity.class);
                intent.putExtra("name", "Chủ đề");
                intent.putExtra("type_id", 1);
                startActivity(intent);
            }
        });
    }
}