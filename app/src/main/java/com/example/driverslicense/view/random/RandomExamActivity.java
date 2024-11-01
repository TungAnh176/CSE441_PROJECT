package com.example.driverslicense.view.random;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.driverslicense.DataLocal.DataMemory;
import com.example.driverslicense.R;
import com.example.driverslicense.adapter.RandomAdapter;
import com.example.driverslicense.api.ApiServices;
import com.example.driverslicense.model.exam.Exam;
import com.example.driverslicense.model.exam.QuestionExam;
import com.example.driverslicense.model.exam.SaveAnwer;
import com.example.driverslicense.model.exam.SaveAnwerResponse;
import com.example.driverslicense.view.content.DetailQuesionActivity;
import com.example.driverslicense.controller.ExamController;
import com.example.driverslicense.view.main.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RandomExamActivity extends AppCompatActivity {
    List<QuestionExam> examList;
    RandomAdapter examAdapter;
    ListView listView;
    Button btnBack;

    Button btnSave;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 1200000;
    TextView txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_random_exam);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtTime = findViewById(R.id.txt_time1);
        btnSave = findViewById(R.id.btnSave1);

        listView = findViewById(R.id.item_list_random);
        btnBack = findViewById(R.id.btn_back_list);
        startTimer();

        btnBack.setOnClickListener(v -> {
            finish();
        });

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Exam.class, new ExamController())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiServices contentService = retrofit.create(ApiServices.class);

        examList = new ArrayList<>();
        examAdapter = new RandomAdapter(this, examList);

        contentService.getExamByID(getIntent().getIntExtra("exam_id", 1)).enqueue(new Callback<Exam>() {
            @Override
            public void onResponse(@NonNull Call<Exam> call, @NonNull Response<Exam> response) {
                Toast.makeText(RandomExamActivity.this, "Success", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful() && response.body() != null) {
                    examList.clear();
                    examList.addAll(response.body().getQuestions());
                    listView.setAdapter(examAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(RandomExamActivity.this, DetailQuesionActivity.class)
                                    .putExtra("id", examList.get(position).getQuestion_id()));
                        }
                    });
                    DataMemory.DATA_SAVE_QUESTION.setUser_id(response.body().getUser_id() + "");
                    DataMemory.DATA_SAVE_QUESTION.setExam_id(response.body().getId() + "");
                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<Exam> call, @NonNull Throwable throwable) {

            }
        });

        btnSave.setOnClickListener(v -> {
            Gson gson1 = new GsonBuilder()
                    .registerTypeAdapter(Exam.class, new ExamController())
                    .create();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit1 = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8000/api/")
                    .addConverterFactory(GsonConverterFactory.create(gson1))
                    .client(client)
                    .build();

            ApiServices apiServices = retrofit1.create(ApiServices.class);

            AlertDialog.Builder builder = new AlertDialog.Builder(RandomExamActivity.this);
            builder.setMessage("Xác nhận nộp bài?");
            builder.setTitle("Thông báo !");
            builder.setCancelable(false);
            builder.setPositiveButton("Đồng ý", (dialog, which) -> {
                apiServices.saveAnwer(DataMemory.DATA_SAVE_QUESTION).enqueue(new Callback<SaveAnwerResponse>() {
                    @Override
                    public void onResponse(Call<SaveAnwerResponse> call, Response<SaveAnwerResponse> response) {
                        DataMemory.DATA_SAVE_QUESTION = new SaveAnwer();
                        String checkPass = response.body().getPass() ? "Đã đạt bài thi" : "Bạn cần ôn tập thi lại";
                        String diem = checkPass + "\n" + response.body().getScore() + "/" + examList.size();

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(RandomExamActivity.this);
                        builder1.setMessage("" + diem);
                        builder1.setTitle("Thông báo số điểm!");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("ok", (dialog, which) -> {

                            dialog.dismiss();
                        });

                        // Create the Alert dialog
                        AlertDialog alertDialog = builder1.create();
                        // Show the Alert Dialog box
                        alertDialog.show();

                    }

                    @Override
                    public void onFailure(Call<SaveAnwerResponse> call, Throwable throwable) {

                    }
                });
                dialog.dismiss();
            });

            builder.setNegativeButton("hủy", (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();
            // Show the Alert Dialog box
            alertDialog.show();
        });

    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        @SuppressLint("DefaultLocale") String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        txtTime.setText(timeLeftFormatted);
    }


    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

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
                apiServices.saveAnwer(DataMemory.DATA_SAVE_QUESTION).enqueue(new Callback<SaveAnwerResponse>() {
                    @Override
                    public void onResponse(Call<SaveAnwerResponse> call, Response<SaveAnwerResponse> response) {
                        DataMemory.DATA_SAVE_QUESTION = new SaveAnwer();
                        String checkPass = response.body().getPass() ? "Đã đạt bài thi" : "Bạn cần ôn tập thi lại";
                        String diem = checkPass + "\n" + response.body().getScore() + "/" + examList.size();

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(RandomExamActivity.this);
                        builder1.setMessage("" + diem);
                        builder1.setTitle("Thông báo số điểm!");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("ok", (dialog, which) -> {
                            Toast.makeText(RandomExamActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RandomExamActivity.this, MainActivity.class));
                            RandomExamActivity.this.finish();
                            dialog.dismiss();
                        });

                        // Create the Alert dialog
                        AlertDialog alertDialog = builder1.create();
                        // Show the Alert Dialog box
                        alertDialog.show();

                    }

                    @Override
                    public void onFailure(Call<SaveAnwerResponse> call, Throwable throwable) {

                    }
                });
                txtTime.setText("Done!");
            }
        }.start();
    }
}