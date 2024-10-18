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
        btnBackA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityA2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupExamButton(){
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
}