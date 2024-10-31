package com.example.driverslicense.view.content;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.driverslicense.R;
import com.example.driverslicense.view.main.ActivityA1;
import com.example.driverslicense.view.main.ActivityA2;

public class ContentActivity extends AppCompatActivity {
    Button btnConcept, btnCulture,btnTechnique,btnSignage,btnSashape,btnParalysis,btnStructure,btnBackContent;
    TextView txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnConcept = findViewById(R.id.btn_concept);
        btnCulture = findViewById(R.id.btn_culture);
        btnTechnique = findViewById(R.id.btn_technique);
        btnSignage = findViewById(R.id.btn_signage);
        btnSashape = findViewById(R.id.btn_sashape);
        btnParalysis = findViewById(R.id.btn_paralysis);
        btnStructure = findViewById(R.id.btn_structure);
        btnBackContent = findViewById(R.id.btn_back_content);
        txtContent = findViewById(R.id.txt_content);

        String txt = getIntent().getStringExtra("name");
        int type = getIntent().getIntExtra("type_id",0);
        txtContent.setText(txt);

        setupBackButton();

        setupContentButton(type);

        setupCultureButton(type);

        setupTechniqueButton(type);

        setupSignageButton(type);

        setupSashapeButton(type);

        setupParalysisButton(type);

        setupStructureButton(type);

    }
    private void setupBackButton(){
        btnBackContent.setOnClickListener(view -> {
            finish();
        });
    }

    private void setupContentButton(int type){
        btnConcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, ListContentActivity.class);
                intent.putExtra("name", "Khái niệm và quy tắc giao thông");
                intent.putExtra("type_id", type); // Đảm bảo 'type' không bị null
                intent.putExtra("category_id", 1); // Đảm bảo giá trị truyền là hợp lệ
                startActivity(intent);
            }
        });
    }

    private void setupCultureButton(int type){
        btnCulture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, ListContentActivity.class);
                intent.putExtra("name", "Văn hóa và đạo đức");
                intent.putExtra("type_id", type);
                intent.putExtra("category_id", 2);
                startActivity(intent);
            }
        });
    }

    private void setupTechniqueButton(int type){
        btnTechnique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, ListContentActivity.class);
                intent.putExtra("name", "Kĩ thuật lái xe");
                intent.putExtra("type_id", type);
                intent.putExtra("category_id", 3);
                startActivity(intent);
            }
        });
    }

    private void setupStructureButton(int type){
        btnStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, ListContentActivity.class);
                intent.putExtra("name", "Cấu tạo và sửa chữa");
                intent.putExtra("type_id", type);
                intent.putExtra("category_id",4 );
                startActivity(intent);
            }
        });
    }

    private void setupSignageButton(int type){
        btnSignage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, ListContentActivity.class);
                intent.putExtra("name", "Biển báo và đường bộ");
                intent.putExtra("type_id", type);
                intent.putExtra("category_id", 5);
                startActivity(intent);
            }
        });
    }

    private void setupSashapeButton(int type){
        btnSashape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, ListContentActivity.class);
                intent.putExtra("name", "Sa hình");
                intent.putExtra("type_id", type);
                intent.putExtra("category_id", 6);
                startActivity(intent);
            }
        });
    }

    private void setupParalysisButton(int type){
        btnParalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, ListContentActivity.class);
                intent.putExtra("name", "Câu liệt");
                intent.putExtra("type_id", type);
                intent.putExtra("category_id", 7);
                startActivity(intent);
            }
        });
    }

}