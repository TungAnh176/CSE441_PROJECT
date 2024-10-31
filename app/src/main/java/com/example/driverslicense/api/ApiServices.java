package com.example.driverslicense.api;

import com.example.driverslicense.model.exam.Exam;
import com.example.driverslicense.model.exam.RandomResponse;
import com.example.driverslicense.model.history.History;
import com.example.driverslicense.model.question.AnswerQuestionHistory;
import com.example.driverslicense.model.question.Answers;
import com.example.driverslicense.model.question.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("exams")
    Call<List<Exam>> getExamsData(
            @Query("exam_type") int examType

    );

    @GET("exams/fixed")
    Call<List<Exam>> getExamsTypeData(
            @Query("is_fixed") int is_fixed,
            @Query("set_fixed_number") int set_fixed_number,
            @Query("exam_type") int exam_type

    );

    @GET("get-user-exam-history")
    Call<List<History>> getUserExamHistory(
            @Query("type_id") int typeId
    );


    @POST("save-user-answers")
    Call<SaveAnwerResponse> saveAnwer(
            @Body SaveAnwer saveAnwer
    );


    @GET("exams/random")
    Call<RandomResponse> getRandom(
            @Query("type_id") int type_id
    );

    @GET("questions/category")
    Call<List<Question>> getListCategory(
            @Query("type_id") int typeId,
            @Query("category_id") int categoryId

    );

    @GET("questions")
    Call<Question> getDetailQuestion(
            @Query("id") int typeId
    );
    @GET("exams/id")
    Call<Exam> getExamByID(
            @Query("id") int typeId
    );

    @POST("check-answer")
    Call<AnswerQuestionHistory> checkAnswer(
            @Body Answers answerRequest // Sử dụng lớp này để gửi câu trả lời
    );
    @GET("questions/type")
    Call<List<Question>> getListQuestion(
            @Query("type_id") int typeId

    );

}
