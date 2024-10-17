//package com.example.driverslicense.view.random;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toolbar;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.driverslicense.R;
//
//public class RandomActivity extends AppCompatActivity {
//    TextView textView1;
//    Button btn_next, btn_back, btn_previous, btn_Nopbai;
//    Toolbar toolbar, toolbar2;
//
//    protected void onShuffe(Bundle savedInstanceState) {
//
//        btn_back = findViewById(R.id.btn_back);
//        btn_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RandomActivity.this, Activity_A1.class);
//                startActivity(intent);
//            }
//        });
//    }
//}
