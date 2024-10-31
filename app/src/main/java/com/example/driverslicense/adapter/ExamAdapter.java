package com.example.driverslicense.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.driverslicense.R;
import com.example.driverslicense.model.exam.Exam;

import java.util.List;

public class ExamAdapter extends ArrayAdapter<Exam> {

    public ExamAdapter(Context context, List<Exam> exams) {
        super(context, 0, exams);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Exam exam = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_exam, parent, false);

        }
        TextView txtID = convertView.findViewById(R.id.txt_id);
        txtID.setText("Bộ đề số " + String.valueOf(exam.getSet_fixed_number()));
        return convertView;
    }

}
