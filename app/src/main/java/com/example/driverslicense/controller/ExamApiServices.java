package com.example.driverslicense.controller;

import com.example.driverslicense.model.Exam;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExamApiServices {
    @GET("api/exams")
    Call<List<Exam>> getExamsData(
            @Query("exam_type") int examType
    );
}
