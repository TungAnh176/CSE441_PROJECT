package com.example.driverslicense.view;

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
import com.example.driverslicense.api.ApiServices;
import com.example.driverslicense.dapater.ExamAdapter;
import com.example.driverslicense.model.Exam;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExamActivity extends AppCompatActivity {
    TextView txtExam;
    Button btnBack;
    ListView listView;
    List<Exam> examList;
    ExamAdapter examAdapter;

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
        fetch(type);
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

    private void fetch(int type){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServices apiServices = retrofit.create(ApiServices.class);

        listView = findViewById(R.id.item_exam);
        examList = new ArrayList<>();
        examAdapter = new ExamAdapter(this, examList);

        apiServices.getExamsData(type).enqueue(new Callback<List<Exam>>() {
            @Override
            public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                Toast.makeText(ExamActivity.this, "Success", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful() && response.body() != null) {
                    examList.clear();  // Xóa danh sách cũ trước khi thêm mới
                    examList.addAll(response.body());  // Thêm dữ liệu vào danh sách
                    examAdapter.notifyDataSetChanged();  // Cập nhật ListView
                } else {
                    // Xử lý trường hợp không thành công

                }
            }

            @Override
            public void onFailure(Call<List<Exam>> call, Throwable throwable) {
                Toast.makeText(ExamActivity.this, "Error"+ throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ExamActivity", "Error fetching exams: ", throwable);
            }
        });
    }

}