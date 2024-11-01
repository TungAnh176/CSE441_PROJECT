package com.example.driverslicense.view.exam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.driverslicense.DataLocal.DataMemory;
import com.example.driverslicense.controller.ExamController;
import com.example.driverslicense.R;
import com.example.driverslicense.adapter.ExamTypeAdapter;
import com.example.driverslicense.api.ApiServices;
import com.example.driverslicense.model.exam.Exam;
import com.example.driverslicense.model.exam.QuestionExam;
import com.example.driverslicense.model.exam.SaveAnwer;
import com.example.driverslicense.model.exam.SaveAnwerResponse;
import com.example.driverslicense.view.content.DetailQuesionActivity;
import com.example.driverslicense.view.main.ActivityA1;
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

public class ExamTypeActivity extends AppCompatActivity {
    ListView listView;
    List<QuestionExam> examList;
    ExamTypeAdapter examAdapter;
    Button btnSave, btnBack;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 1200000;
    TextView txtTime;
    ApiServices apiServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exam_type);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtTime = findViewById(R.id.txt_time);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btn_back_exam1);
        int isFixed = getIntent().getIntExtra("is_fixed", 1);
        int id = getIntent().getIntExtra("id", 1);
        int examId = getIntent().getIntExtra("exam_id", 0);
        startTimer();
        getExam(isFixed, id, examId);

    }
    private void getExam(int isFixed, int id, int examId) {
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

        apiServices = retrofit.create(ApiServices.class);

        listView = findViewById(R.id.item_exam_type);
        examList = new ArrayList<>();
        examAdapter = new ExamTypeAdapter(this, examList);

        apiServices.getExamsTypeData(isFixed, id, examId).enqueue(new Callback<List<Exam>>() {
            @Override
            public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                Log.d("ExamActivity", "Response received: " + getIntent().getIntExtra("id", 1));
                Toast.makeText(ExamTypeActivity.this, "Success", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful() && response.body() != null) {
                    examList.clear();
                    examList.addAll(response.body().get(0).getQuestions());
                    listView.setAdapter(examAdapter);
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        Log.d("ListView", "Item clicked at position: " + position);
                        startActivity(new Intent(ExamTypeActivity.this, DetailQuestionExamActivity.class)
                                .putExtra("id", examList.get(position).getQuestion_id())
                                .putExtra("chude", true));
                    });
                    DataMemory.DATA_SAVE_QUESTION.setUser_id(response.body().get(0).getUser_id() + "");
                    DataMemory.DATA_SAVE_QUESTION.setExam_id(response.body().get(0).getId() + "");
                } else {


                }
            }

            @Override
            public void onFailure(Call<List<Exam>> call, Throwable throwable) {
                Toast.makeText(ExamTypeActivity.this, "Error" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ExamActivity", "Error fetching exams: ", throwable);
            }
        });


        btnSave.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ExamTypeActivity.this);
            builder.setMessage("Xác nhận nộp bài?");
            builder.setTitle("Thông báo !");
            builder.setCancelable(false);
            builder.setPositiveButton("Đồng ý", (dialog, which) -> {
                apiServices.saveAnwer(DataMemory.DATA_SAVE_QUESTION).enqueue(new Callback<SaveAnwerResponse>() {
                    @Override
                    public void onResponse(Call<SaveAnwerResponse> call, Response<SaveAnwerResponse> response) {
                        DataMemory.DATA_SAVE_QUESTION = new SaveAnwer();
                        String checkPass = response.body().getPass() ? "Đã đạt bài thi" : "Bạn cần ôn tập thi lại";
                        Log.d("API Response", "User ID: " + DataMemory.DATA_SAVE_QUESTION.getUser_id());
                        Log.d("API Response", "Exam ID: " + DataMemory.DATA_SAVE_QUESTION.getExam_id());
                        Log.d("ExamActivity", "Response received: " + response.body().getScore());
                        String diem = checkPass + "\n" + response.body().getScore() + "/" + examList.size();

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ExamTypeActivity.this);
                        builder1.setMessage("" + diem);
                        builder1.setTitle("Thông báo số điểm!");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("ok", (dialog, which) -> {
                            dialog.dismiss();
                            ExamTypeActivity.this.finish();
                        });

                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();

                    }

                    @Override
                    public void onFailure(Call<SaveAnwerResponse> call, Throwable throwable) {

                    }
                });
                dialog.dismiss();
            });

            builder.setNegativeButton("hủy", (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    private void back() {
        btnBack.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ExamTypeActivity.this);
            builder.setMessage("Bạn có chắc chắn muốn thoát không?");
            builder.setTitle("Xác nhận thoát");
            builder.setCancelable(false);

            builder.setPositiveButton("Có", (dialog, which) -> {
                apiServices.saveAnwer(DataMemory.DATA_SAVE_QUESTION).enqueue(new Callback<SaveAnwerResponse>() {
                    @Override
                    public void onResponse(Call<SaveAnwerResponse> call, Response<SaveAnwerResponse> response) {
                        DataMemory.DATA_SAVE_QUESTION = new SaveAnwer();
                        String checkPass = response.body().getPass() ? "Đã đạt bài thi" : "Bạn cần ôn tập thi lại";
                        Log.d("API Response", "User ID: " + DataMemory.DATA_SAVE_QUESTION.getUser_id());
                        Log.d("API Response", "Exam ID: " + DataMemory.DATA_SAVE_QUESTION.getExam_id());
                        Log.d("ExamActivity", "Response received: " + response.body().getScore());
                        String diem = checkPass + "\n" + response.body().getScore() + "/" + examList.size();

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ExamTypeActivity.this);
                        builder1.setMessage("" + diem);
                        builder1.setTitle("Thông báo số điểm!");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("ok", (dialog, which) -> {
                            dialog.dismiss();
                            ExamTypeActivity.this.finish();
                        });

                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();

                    }

                    @Override
                    public void onFailure(Call<SaveAnwerResponse> call, Throwable throwable) {

                    }
                });
                dialog.dismiss();
                finish();
            });

            builder.setNegativeButton("Không", (dialog, which) -> {

                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
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

                apiServices = retrofit.create(ApiServices.class);
                apiServices.saveAnwer(DataMemory.DATA_SAVE_QUESTION).enqueue(new Callback<SaveAnwerResponse>() {
                    @Override
                    public void onResponse(Call<SaveAnwerResponse> call, Response<SaveAnwerResponse> response) {
                        DataMemory.DATA_SAVE_QUESTION = new SaveAnwer();
                        String checkPass = response.body().getPass() ? "Đã đạt bài thi" : "Bạn cần ôn tập thi lại";
                        String diem = checkPass + "\n" + response.body().getScore() + "/" + examList.size();

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ExamTypeActivity.this);
                        builder1.setMessage("" + diem);
                        builder1.setTitle("Thông báo số điểm!");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("ok", (dialog, which) -> {
                            Toast.makeText(ExamTypeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ExamTypeActivity.this, ExamActivity.class));

                            ExamTypeActivity.this.finish();
                            dialog.dismiss();
                            dialog.dismiss();
                        });

                        AlertDialog alertDialog = builder1.create();
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