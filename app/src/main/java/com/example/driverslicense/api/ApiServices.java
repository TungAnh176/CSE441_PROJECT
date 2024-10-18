package com.example.driverslicense.api;

import com.example.driverslicense.model.Exam;
import com.example.driverslicense.model.History;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("exams")
    Call<List<Exam>> getExamsData(
            @Query("exam_type") int examType

    );

    @GET("get-user-exam-history")
    Call<List<History>> getUserExamHistory(
            @Query("type_id") int typeId
    );
}
