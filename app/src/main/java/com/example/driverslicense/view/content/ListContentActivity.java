package com.example.driverslicense.view.content;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.driverslicense.adapter.ListContentAdapter;

import com.example.driverslicense.model.question.Question;
import com.example.driverslicense.controller.QuestionController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListContentActivity extends AppCompatActivity {
    Button btnBack;
    TextView txtListContent;
    ListView listView;
    List<Question> questions;
    ListContentAdapter listContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btn_back_list_content);
        txtListContent = findViewById(R.id.txt_list_content);
        listView = findViewById(R.id.item_list_content_id);

        String txt = getIntent().getStringExtra("name");
        int type = getIntent().getIntExtra("type_id", 0);
        int category = getIntent().getIntExtra("category_id", 0);

        txtListContent.setText(txt);

        setupBackButton();

        fecthCategory(type, category);
    }

    private void fecthCategory(int type, int category) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Question.class, new QuestionController())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiServices contentService = retrofit.create(ApiServices.class);

        questions = new ArrayList<>();
        listContentAdapter = new ListContentAdapter(this, questions);

        contentService.getListCategory(type, category).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                Toast.makeText(ListContentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful() && response.body() != null) {
                    questions.clear();
                    questions.addAll(response.body());
                    listView.setAdapter(listContentAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            startActivity(new Intent(ListContentActivity.this, DetailQuesionActivity.class)
//                                    .putExtra("id", questions.get(position).getId()));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable throwable) {
                Toast.makeText(ListContentActivity.this, "Error" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("QuestionActivity", "Error fetching exams: ", throwable);
            }
        });
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(view -> {

            finish();
        });
    }

}