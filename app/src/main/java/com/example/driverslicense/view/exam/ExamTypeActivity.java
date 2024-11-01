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
import com.example.driverslicense.model.exam.SaveAnswer;
import com.example.driverslicense.model.exam.SaveAnswerResponse;
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

        setupView();
        setupApiServices();
        setupTimer();

        int isFixed = getIntent().getIntExtra("is_fixed", 1);
        int id = getIntent().getIntExtra("id", 1);
        int examId = getIntent().getIntExtra("exam_id", 0);

        fetchExamData(isFixed, id, examId);
        setupSaveButton();
        setupBackButton();

    }
    private void setupView() {
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
        listView = findViewById(R.id.item_exam_type);
    }

    private void setupApiServices() {
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
    }

    private void setupTimer() {
        startTimer();
    }

    private void fetchExamData(int isFixed, int id, int examId) {
        examList = new ArrayList<>();
        examAdapter = new ExamTypeAdapter(this, examList);

        apiServices.getExamsTypeData(isFixed, id, examId).enqueue(new Callback<List<Exam>>() {
            @Override
            public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                handleExamResponse(response);
            }

            @Override
            public void onFailure(Call<List<Exam>> call, Throwable throwable) {
                handleExamError(throwable);
            }
        });
    }

    private void handleExamResponse(Response<List<Exam>> response) {
        if (response.isSuccessful() && response.body() != null) {
            examList.clear();
            examList.addAll(response.body().get(0).getQuestions());
            listView.setAdapter(examAdapter);
            setupListViewClickListener();

            DataMemory.DATA_SAVE_QUESTION.setUser_id(response.body().get(0).getUser_id() + "");
            DataMemory.DATA_SAVE_QUESTION.setExam_id(response.body().get(0).getId() + "");
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to load exam data", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleExamError(Throwable throwable) {
        Toast.makeText(this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e("ExamActivity", "Error fetching exams: ", throwable);
    }

    private void setupListViewClickListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position < examList.size()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamTypeActivity.this);
                builder.setMessage("Bạn có chắc chắn muốn làm bài này không?")
                        .setTitle("Xác nhận làm bài")
                        .setCancelable(false)
                        .setPositiveButton("Đồng ý", (dialog, which) -> {
                            Intent intent = new Intent(ExamTypeActivity.this, DetailQuestionExamActivity.class);
                            intent.putExtra("id", examList.get(position).getQuestion_id());
                            intent.putExtra("chude", true);
                            startActivity(intent);
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
                builder.show();
            } else {
                Log.e("ExamTypeActivity", "Invalid position clicked: " + position);
            }
        });
    }


    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> showSubmitConfirmationDialog());
    }

    private void showSubmitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Xác nhận nộp bài?")
                .setTitle("Thông báo!")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", (dialog, which) -> submitAnswers())
                .setNegativeButton("Hủy", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void submitAnswers() {
        apiServices.saveAnswer(DataMemory.DATA_SAVE_QUESTION).enqueue(new Callback<SaveAnswerResponse>() {
            @Override
            public void onResponse(Call<SaveAnswerResponse> call, Response<SaveAnswerResponse> response) {
                handleAnswerSubmissionResponse(response);
            }

            @Override
            public void onFailure(Call<SaveAnswerResponse> call, Throwable throwable) {
                // Handle error if needed
            }
        });
    }

    private void handleAnswerSubmissionResponse(Response<SaveAnswerResponse> response) {
        if (response.isSuccessful() && response.body() != null) {
            DataMemory.DATA_SAVE_QUESTION = new SaveAnswer();
            String resultMessage = generateResultMessage(response.body());
            showResultDialog(resultMessage);
        }
    }

    private String generateResultMessage(SaveAnswerResponse response) {
        String checkPass = response.getPass() ? "Đã đạt bài thi" : "Bạn cần ôn tập thi lại";
        return checkPass + "\n" + response.getScore() + "/" + examList.size();
    }

    private void showResultDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle("Thông báo số điểm!")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show();
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(v -> showExitConfirmationDialog());
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Bạn có chắc chắn muốn thoát không?")
                .setTitle("Xác nhận thoát")
                .setCancelable(false)
                .setPositiveButton("Có", (dialog, which) -> submitAnswersAndExit())
                .setNegativeButton("Không", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void submitAnswersAndExit() {
        submitAnswers();
        finish();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
<<<<<<< HEAD
                handleTimerFinish();
=======

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
>>>>>>> 2e80f9fd87e5071a3cf4ef4942ca37abea18df84
            }
        }.start();
    }

    private void updateTimerDisplay() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        txtTime.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void handleTimerFinish() {
        submitAnswers();
        txtTime.setText("Done!");
    }

}