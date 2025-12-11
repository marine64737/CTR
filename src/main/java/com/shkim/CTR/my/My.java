package com.shkim.CTR.my;

import jakarta.validation.constraints.NotNull;

public class My {

    private int id;

    private int userId;

    public My(int id, int userId){
        this.id=id;
        this.userId= userId;
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

}
