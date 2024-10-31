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

public class ActivityA2 extends AppCompatActivity {
    Button btnBackA2, btnRandomA2, btnExamA2, btnListA2, btnContentA2, btnHistoryA2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_a2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBackA2 = findViewById(R.id.btn_back_a2);
        btnRandomA2 = findViewById(R.id.btn_random_a2);
        btnExamA2 = findViewById(R.id.btn_exam_a2);
        btnListA2 = findViewById(R.id.btn_list_a2);
        btnContentA2 = findViewById(R.id.btn_content_a2);
        btnHistoryA2 = findViewById(R.id.btn_history_a2);
        // quay lại giao diện chính
        setupBackButton();
        // chuyển đến giao diện exam
        setupExamButton();

        setupHistoryButton();

        setBtnListA2();

        setupContentButton();

        btnRandomA2.setOnClickListener(v -> {
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
            apiServices.getRandom(2).enqueue(new Callback<RandomResponse>() {
                @Override
                public void onResponse(Call<RandomResponse> call, Response<RandomResponse> response) {
                    startActivity(new Intent(ActivityA2.this, RandomExamActivity.class)
                            .putExtra("exam_id", response.body().getExam_id()));
                }

                @Override
                public void onFailure(Call<RandomResponse> call, Throwable throwable) {

                }
            });

        });

    }

    private void setupHistoryButton() {
        btnHistoryA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityA2.this, HistoryActivity.class);
                intent.putExtra("id", 2);
                startActivity(intent);
            }
        });
    }

    private void setupBackButton() {
        btnBackA2.setOnClickListener(view -> {
            finish();
        });
    }

    private void setupExamButton() {
        btnExamA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityA2.this, ExamActivity.class);
                intent.putExtra("exam_name", "Bộ đề A2");
                intent.putExtra("exam_id", 2);
                startActivity(intent);
            }
        });
    }



    private void setBtnListA2() {

        btnListA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityA2.this, QuestionActivity.class);
                intent.putExtra("name", "450 câu");
                intent.putExtra("type_id", 2);
                startActivity(intent);
            }
        });
    }

    private void setupContentButton() {
        btnContentA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityA2.this, ContentActivity.class);
                intent.putExtra("name", "Chủ đề");
                intent.putExtra("category_id", 2);
                startActivity(intent);
            }
        });
    }
}