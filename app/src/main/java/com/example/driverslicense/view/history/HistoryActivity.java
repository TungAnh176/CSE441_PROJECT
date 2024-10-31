package com.example.driverslicense.view.history;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.driverslicense.R;
import com.example.driverslicense.adapter.HistoryAdapter;
import com.example.driverslicense.api.ApiServices;
import com.example.driverslicense.controller.HistoryController;
import com.example.driverslicense.model.history.History;
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

public class HistoryActivity extends AppCompatActivity {
    Button btnBackHistory;
    ListView listView;
    List<History> historyList;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBackHistory = findViewById(R.id.btn_back_history);
        int typeID = getIntent().getIntExtra("id", 0);
        setupBackButton();
        fetchHisory(typeID);
    }

    private void fetchHisory(int typeID) {

        listView = findViewById(R.id.item_history);
        historyList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, historyList);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(History.class, new HistoryController())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson)) // Thêm GSON tùy chỉnh
                .build();

        ApiServices apiServices = retrofit.create(ApiServices.class);

        apiServices.getUserExamHistory(typeID).enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                Toast.makeText(HistoryActivity.this, "Success", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful() && response.body() != null) {
                    historyList.clear();
                    historyList.addAll(response.body());
                    listView.setAdapter(historyAdapter);
                } else {


                }
            }

            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "Error"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HistoryActivity", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setupBackButton() {
        btnBackHistory.setOnClickListener(view -> {
            Intent intent = new Intent(HistoryActivity.this,
                    getIntent().getIntExtra("id", 0) == 1 ? ActivityA1.class : ActivityA2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish(); // Có thể bỏ dòng này nếu không cần
        });
    }

}