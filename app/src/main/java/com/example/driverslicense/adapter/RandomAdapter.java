package com.example.driverslicense.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.driverslicense.R;
import com.example.driverslicense.model.exam.QuestionExam;

import java.util.List;

public class RandomAdapter extends ArrayAdapter<QuestionExam> {

    public RandomAdapter(Context context, List<QuestionExam> exams) {
        super(context, 0, exams);
    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        QuestionExam exam = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_exam, parent, false);

        }
        TextView txtID = convertView.findViewById(R.id.txt_id);
        txtID.setText("Câu hỏi số " + (exam != null ? exam.getQuestion_id() : 0));
        return convertView;
    }

}
