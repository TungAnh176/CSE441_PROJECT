package com.example.driverslicense.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.driverslicense.R;
import com.example.driverslicense.controller.ExamApiServices;
import com.example.driverslicense.model.Exam;
import com.example.driverslicense.model.QuestionExam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExamActivity extends AppCompatActivity {
    TextView txtExam;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exam);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtExam = findViewById(R.id.txt_exam);
        String txt = getIntent().getStringExtra("exam_name");
        int type = getIntent().getIntExtra("exam_id", 0);
        txtExam.setText(txt);
        btnBack = findViewById(R.id.btn_back_exam);

        setupBackButton();
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(view -> {
            Intent intent = (getIntent().getIntExtra("exam_id", 0) == 1)
                    ? new Intent(ExamActivity.this, ActivityA1.class)
                    : new Intent(ExamActivity.this, ActivityA2.class);
            startActivity(intent);
            finish();
        });
    }

}