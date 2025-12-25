package com.shkim.CTR.question;

public class ProblemDTO {

    private int id;

    private String title;

    private String url;

    public ProblemDTO(int id, String title, String url){
        this.id = id;
        this.title=title;
        this.url=url;
    }

//    public Question(String num, String title){
//        this.num=num;
//        this.title=title;
//    }
    public int getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url=url;
    }

    @Override
    public String toString(){
        return String.format("Question[No.=%d, Title='%s']", id, title);
    }
}
