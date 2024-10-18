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
import com.example.driverslicense.view.exam.ExamActivity;
import com.example.driverslicense.view.history.HistoryActivity;
import com.example.driverslicense.view.question.QuestionActivity;

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

        setBtnBack();
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityA1.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupExamButton(){
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

    private void setBtnBack(){
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
}