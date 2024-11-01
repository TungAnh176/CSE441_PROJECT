package com.example.driverslicense.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.driverslicense.R;

import com.example.driverslicense.model.history.History;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<History> {

    public HistoryAdapter(Context context, List<History> historyList) {
        super(context, 0, historyList);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        History history = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_history, parent, false);
        }

        // Tìm các thành phần giao diện trong convertView
        TextView txtID = convertView.findViewById(R.id.txt_exam_id);
        TextView txtScore = convertView.findViewById(R.id.txt_score);
        TextView txtPass = convertView.findViewById(R.id.txt_pass);

        if (history.getPass()) {

            txtPass.setText("Kết quả: Qua");
            txtPass.setTextColor(Color.GREEN);
            txtID.setTextColor(Color.GREEN);
            txtScore.setTextColor(Color.GREEN);
        } else {
            txtPass.setText("Kết quả: Trượt");
            txtPass.setTextColor(Color.RED);
            txtID.setTextColor(Color.RED);
            txtScore.setTextColor(Color.RED);
        }
        // Đặt giá trị cho các TextView
        int examId = history.getExam_id();

        // Chuyển đổi exam_id từ 9-18 thành 1-10
        int displayId = (examId >= 9 && examId <= 18) ? examId - 8 : examId;

        txtID.setText("Bài số: " + String.valueOf(displayId));
        txtScore.setText("Điểm số: " + String.valueOf(history.getScore()));

        return convertView;
    }


}
