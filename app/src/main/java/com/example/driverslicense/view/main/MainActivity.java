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

public class MainActivity extends AppCompatActivity {
        Button btnA1, btnA2;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            btnA1 = findViewById(R.id.btn_A1);
            btnA2 = findViewById(R.id.btn_A2);
            //nút chuyển đến màn hình giao diện A1
            setUpNextButtonA1();
            //nút chuyển đến màn hình giao diện A2
            setUpNextButtonA2();
        }

        private void setUpNextButtonA1() {
            btnA1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ActivityA1.class);
                    startActivity(intent);
                }
            });
        }

        private void setUpNextButtonA2() {
            btnA2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ActivityA2.class);
                    startActivity(intent);
                }
            });
        }

}