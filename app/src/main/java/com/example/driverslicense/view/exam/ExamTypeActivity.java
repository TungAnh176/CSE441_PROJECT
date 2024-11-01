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

        txtTime = findViewById(R.id.txt_time);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btn_back_exam1);
        listView = findViewById(R.id.item_exam_type);
    }

    private void setupApiServices() {
        // Thiết lập Retrofit và Gson cho việc gọi API
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Exam.class, new ExamController())
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/") // Địa chỉ API nội bộ
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        apiServices = retrofit.create(ApiServices.class); // Khởi tạo dịch vụ API
    }

    private void setupTimer() {
        startTimer();  // Bắt đầu đồng hồ đếm ngược
    }

    private void fetchExamData(int isFixed, int id, int examId) {
        examList = new ArrayList<>();
        examAdapter = new ExamTypeAdapter(this, examList);

        // Gọi API để lấy dữ liệu bài thi và xử lý phản hồi
        apiServices.getExamsTypeData(isFixed, id, examId).enqueue(new Callback<List<Exam>>() {
            @Override
            public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                handleExamResponse(response);
            }

            @Override
            public void onFailure(Call<List<Exam>> call, Throwable throwable) {
                // Xử lý lỗi khi không thể lấy dữ liệu bài thi
            }
        });
    }

    private void handleExamResponse(Response<List<Exam>> response) {
        if (response.isSuccessful() && response.body() != null) {
            examList.clear();
            examList.addAll(response.body().get(0).getQuestions()); // Lấy danh sách câu hỏi
            listView.setAdapter(examAdapter);
            setupListViewClickListener();

            // Lưu lại ID người dùng và ID bài thi
            DataMemory.DATA_SAVE_QUESTION.setUser_id(response.body().get(0).getUser_id() + "");
            DataMemory.DATA_SAVE_QUESTION.setExam_id(response.body().get(0).getId() + "");
        } else {
        }
    }



    private void setupListViewClickListener() {
        // Thiết lập sự kiện khi chọn một câu hỏi trong danh sách
        listView.setOnItemClickListener((parent, view, position, id) -> {
            startActivity(new Intent(this, DetailQuestionExamActivity.class)
                    .putExtra("id", examList.get(position).getQuestion_id())
                    .putExtra("chude", true));
        });
    }

    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> showSubmitConfirmationDialog()); // Xác nhận nộp bài
    }

    private void showSubmitConfirmationDialog() {
        // Hiển thị hộp thoại xác nhận nộp bài
        new AlertDialog.Builder(this)
                .setMessage("Xác nhận nộp bài?")
                .setTitle("Thông báo!")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", (dialog, which) -> submitAnswers())
                .setNegativeButton("Hủy", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void submitAnswers() {
        // Gửi dữ liệu trả lời đến máy chủ thông qua API
        apiServices.saveAnswer(DataMemory.DATA_SAVE_QUESTION).enqueue(new Callback<SaveAnswerResponse>() {
            @Override
            public void onResponse(Call<SaveAnswerResponse> call, Response<SaveAnswerResponse> response) {
                handleAnswerSubmissionResponse(response);
            }

            @Override
            public void onFailure(Call<SaveAnswerResponse> call, Throwable throwable) {
                // Xử lý lỗi khi không thể gửi câu trả lời
            }
        });
    }

    private void handleAnswerSubmissionResponse(Response<SaveAnswerResponse> response) {
        // Xử lý phản hồi sau khi nộp bài thành công
        if (response.isSuccessful() && response.body() != null) {
            DataMemory.DATA_SAVE_QUESTION = new SaveAnswer();  // Xóa dữ liệu tạm
            String resultMessage = generateResultMessage(response.body());
            showResultDialog(resultMessage);  // Hiển thị điểm số và trạng thái thi đậu hay không
        }
    }

    private String generateResultMessage(SaveAnswerResponse response) {
        // Tạo thông báo kết quả bài thi
        String checkPass = response.getPass() ? "Đã đạt bài thi" : "Bạn cần ôn tập thi lại";
        return checkPass + "\n" + response.getScore() + "/" + examList.size();
    }

    private void showResultDialog(String message) {
        // Hiển thị hộp thoại với kết quả bài thi
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle("Thông báo số điểm!")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show();
    }

    private void setupBackButton() {
        // Thiết lập nút quay lại và hiển thị hộp thoại xác nhận
        btnBack.setOnClickListener(v -> showExitConfirmationDialog());
    }

    private void showExitConfirmationDialog() {
        // Hiển thị hộp thoại xác nhận thoát
        new AlertDialog.Builder(this)
                .setMessage("Bạn có muốn lưu bài thi?")
                .setTitle("Thông báo!")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", (dialog, which) -> submitAnswers())
                .setNegativeButton("Hủy", (dialog, which) -> finish())
                .show();
    }

    private void startTimer() {
        // Bắt đầu đồng hồ đếm ngược với thời gian cài đặt
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();  // Cập nhật hiển thị thời gian mỗi giây
            }

            @Override
            public void onFinish() {
                handleTimerFinish();  // Xử lý khi hết thời gian
            }
        }.start();
    }

    private void handleTimerFinish() {
        txtTime.setText("Done!");  // Hiển thị thông báo hết giờ
        submitAnswers();  // Tự động nộp bài thi khi hết giờ
    }

    @SuppressLint("DefaultLocale")
    private void updateTimerDisplay() {
        // Định dạng và hiển thị thời gian còn lại
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        txtTime.setText(String.format("%02d:%02d", minutes, seconds));
    }

}