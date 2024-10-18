package com.example.driverslicense.api;

import com.example.driverslicense.model.Exam;
import com.example.driverslicense.model.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ListQuestionService {
    @GET("questions/type")
    Call<List<Question>> getListQuestion(
            @Query("type_id") int typeId

    );
}
