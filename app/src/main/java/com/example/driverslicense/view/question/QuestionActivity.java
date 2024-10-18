package com.example.driverslicense.view.question;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.driverslicense.R;
import com.example.driverslicense.adapter.ExamAdapter;
import com.example.driverslicense.adapter.ListQuestionAdapter;
import com.example.driverslicense.api.ApiServices;
import com.example.driverslicense.api.ListQuestionService;
import com.example.driverslicense.model.Exam;
import com.example.driverslicense.model.Question;
import com.example.driverslicense.view.exam.ExamActivity;
import com.example.driverslicense.view.exam.ExamDeserializer;
import com.example.driverslicense.view.main.ActivityA1;
import com.example.driverslicense.view.main.ActivityA2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionActivity extends AppCompatActivity {
    Button btnBack;
    TextView txtQuestion;
    ListView listView;
    List<Question> questions;
    ListQuestionAdapter listQuestionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btn_back_question);
        txtQuestion = findViewById(R.id.txt_question);
        listView = findViewById(R.id.item_question_id);

        String txt = getIntent().getStringExtra("name");
        int type = getIntent().getIntExtra("type_id", 0);
        txtQuestion.setText(txt);

        setupBackButton();
        fetchListQuestion(type);
    }

    private void fetchListQuestion(int type) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Question.class, new QuestionDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ListQuestionService listQuestionService = retrofit.create(ListQuestionService.class);

        questions = new ArrayList<>();
        listQuestionAdapter = new ListQuestionAdapter (this, questions);

        listQuestionService.getListQuestion(type).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                Toast.makeText(QuestionActivity.this, "Success", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful() && response.body() != null) {
                    questions.clear();
                    questions.addAll(response.body());
                    listView.setAdapter(listQuestionAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable throwable) {
                Toast.makeText(QuestionActivity.this, "Error"+ throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("QuestionActivity", "Error fetching exams: ", throwable);
            }
        });
    }

    private void setupBackButton(){
        btnBack.setOnClickListener(view -> {
            Intent intent = (getIntent().getIntExtra("type_id", 0) == 1)
                    ? new Intent(QuestionActivity.this, ActivityA1.class)
                    : new Intent(QuestionActivity.this, ActivityA2.class);
            startActivity(intent);
            finish();
        });
    }
}