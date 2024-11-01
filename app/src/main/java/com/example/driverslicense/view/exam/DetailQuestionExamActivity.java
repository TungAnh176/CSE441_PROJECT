package com.example.driverslicense.view.exam;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.driverslicense.DataLocal.DataMemory;
import com.example.driverslicense.R;
import com.example.driverslicense.api.ApiServices;

import com.example.driverslicense.controller.QuestionController;
import com.example.driverslicense.model.exam.QuestionSave;
import com.example.driverslicense.model.question.Option;
import com.example.driverslicense.model.question.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailQuestionExamActivity extends AppCompatActivity {

    // Khởi tạo các hằng số và biến cho hoạt động


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
        imgHinh = findViewById(R.id.imgQuestion);
        optionsContainer = findViewById(R.id.llOption);
        txtDescription = findViewById(R.id.txtDescription);
        imgBack = findViewById(R.id.imgBack);
    }

    private void setupBackButton() {
        imgBack.setOnClickListener(v -> finish());
    }

    private void fetchQuestion(int questionId) {
        // Tạo Retrofit và GSON để cấu hình các request và response API
        Gson gson = buildGson();
        Retrofit retrofit = buildRetrofit(gson);

        apiInterface = retrofit.create(ApiServices.class);
        // Gọi API để lấy chi tiết câu hỏi
        apiInterface.getDetailQuestion(questionId).enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful() && response.body() != null) {
                    question = response.body();  // Lưu đối tượng câu hỏi lấy được
                    displayQuestionContent();  // Hiển thị nội dung câu hỏi
                    displayOptions();  // Hiển thị các lựa chọn trả lời
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
        // Cấu hình Gson để deserializing dữ liệu với lớp QuestionController
        return new GsonBuilder()
                .registerTypeAdapter(Question.class, new QuestionController())
                .create();
    }

    private Retrofit buildRetrofit(Gson gson) {
        // Cấu hình Retrofit với logging cho request/response và converter GSON
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")  // Địa chỉ API
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    private void displayQuestionContent() {
        // Hiển thị nội dung câu hỏi và lấy câu trả lời đã lưu
        textView.setText(question.getContent());
        selectedAnswer = getSavedAnswerForQuestion(questionId);  // Lấy câu trả lời đã lưu từ dữ liệu
    }

    private void displayOptions() {
        // Xóa tất cả các lựa chọn cũ và hiển thị lại các lựa chọn mới
        optionsContainer.removeAllViews();
        List<Option> options = question.getOptions();

        for (Option option : options) {
            if (option.getImage() != null) {
                loadOptionImage(option.getImage());  // Tải ảnh của lựa chọn nếu có
            } else {
                imgHinh.setVisibility(View.GONE);  // Ẩn ImageView nếu không có ảnh
            }
            createOptionButtons(option);  // Tạo nút cho từng lựa chọn
        }
    }

    private void loadOptionImage(String imageUrl) {
        // Sử dụng thư viện Glide để tải ảnh từ URL và hiển thị
        imgHinh.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .into(imgHinh);
    }

    private void createOptionButtons(Option option) {
        // Tạo các nút cho từng lựa chọn nếu có các đáp án a, b, c, d
        if (option.getA() != null) createOptionButton("a: " + option.getA(), "a");
        if (option.getB() != null) createOptionButton("b: " + option.getB(), "b");
        if (option.getC() != null) createOptionButton("c: " + option.getC(), "c");
        if (option.getD() != null) createOptionButton("d: " + option.getD(), "d");
    }

    private String getSavedAnswerForQuestion(int questionId) {
        // Lấy câu trả lời đã lưu cho câu hỏi dựa trên ID từ DataMemory
        for (QuestionSave answer : DataMemory.DATA_SAVE_QUESTION.getAnswers()) {
            if (answer.getQuestion_id() == questionId) {
                return answer.getUser_answer();
            }
        }
        return null;
    }

    private void createOptionButton(String optionText, String optionLabel) {
        // Tạo nút cho một lựa chọn, với tùy chỉnh màu sắc và sự kiện khi bấm vào
        Button button = new Button(DetailQuestionExamActivity.this);
        button.setText(optionText);

        styleOptionButton(button, optionLabel);  // Gọi hàm để thiết lập giao diện nút
        button.setOnClickListener(v -> onOptionSelected(button, optionLabel));  // Xử lý sự kiện khi chọn nút

        optionsContainer.addView(button);  // Thêm nút vào container
    }

    private void styleOptionButton(Button button, String optionLabel) {
        // Đặt màu nền của nút tùy thuộc vào việc nó có được chọn hay không
        if (optionLabel.equals(selectedAnswer)) {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.green));  // Nếu đã chọn, đặt màu xanh lá
        } else {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));  // Nếu chưa chọn, đặt màu xám
        }

        // Thiết lập kích thước và khoảng cách của nút
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 16, 0, 16);
        button.setLayoutParams(layoutParams);
    }

    private void onOptionSelected(Button button, String optionLabel) {
        // Khi chọn một lựa chọn, đặt lại màu cho các nút khác và lưu câu trả lời
        resetOptionButtonColors();
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.green));

        selectedAnswer = optionLabel;
        saveAnswerForQuestion(questionId, optionLabel);
    }

    private void resetOptionButtonColors() {
        // Đặt lại màu của tất cả các nút về màu xám khi chưa chọn
        for (int i = 0; i < optionsContainer.getChildCount(); i++) {
            View child = optionsContainer.getChildAt(i);
            if (child instanceof Button) {
                child.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            }
        }
    }

    private void saveAnswerForQuestion(int questionId, String answer) {
        // Lưu câu trả lời của người dùng cho câu hỏi vào DataMemory
        DataMemory.DATA_SAVE_QUESTION.getAnswers().removeIf(q -> q.getQuestion_id() == questionId);
        DataMemory.DATA_SAVE_QUESTION.getAnswers().add(new QuestionSave(questionId, answer));
    }
}
