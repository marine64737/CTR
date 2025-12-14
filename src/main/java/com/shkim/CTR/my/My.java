package com.shkim.CTR.my;

import jakarta.validation.constraints.NotNull;

public class My {

    private int id;

    private int userId;

    private int questionId;

    private String language;

    private String code;

    private String startDate;

    private String endDate;

    public My(int id, int userId, int questionId, String startDate, String endDate){
        this.id=id;
        this.userId= userId;
        this.questionId= questionId;
        this.startDate= startDate;
        this.endDate = endDate;
    }
    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getUserId(){
        return this.userId;
    }

    public void setUserId(int userId){
        this.userId=userId;
    }

    public int getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(int questionId){
        this.questionId = questionId;
    }

}
