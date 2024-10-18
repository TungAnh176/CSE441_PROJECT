package com.example.driverslicense.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.driverslicense.R;
import com.example.driverslicense.model.History;
import com.example.driverslicense.model.Question;

import java.util.List;

public class ListQuestionAdapter extends ArrayAdapter<Question> {
    public ListQuestionAdapter(Context context, List<Question> questionList) {
        super(context, 0, questionList);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        Question question = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_question_id, parent, false);
        }
        TextView txtId = convertView.findViewById(R.id.txt_question_id);
        //txtId.setText("Câu hỏi số " + String.valueOf(question.getId()));
        txtId.setText("Câu hỏi số " + (position + 1));
        return convertView;
    }
}
