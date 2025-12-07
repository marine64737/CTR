package com.shkim.CTR.question;

import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class Question {

    @NotNull
    private int id;

    private String title;

    public Question(int id, String title){
        this.id=id;
        this.title=title;
    }
    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    @Override
    public String toString(){
        return String.format("Question[No.=%d, Title='%s']", id, title);
    }
}
