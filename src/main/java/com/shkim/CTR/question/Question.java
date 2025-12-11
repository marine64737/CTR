package com.shkim.CTR.question;

public class Question {

    private Integer id;

    private String num;

    private String title;

    public Question(Integer id, String num, String title){
        this.id = id;
        this.num=num;
        this.title=title;
    }

//    public Question(String num, String title){
//        this.num=num;
//        this.title=title;
//    }
    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getNum(){
        return this.num;
    }

    public void setNum(String num){
        this.num = num;
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
