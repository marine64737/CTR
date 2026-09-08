package com.shkim.CTR.Member;

import java.io.Serializable;

public class Member implements Serializable {

    private int id;

    private String name;

    private String password;

    public Member(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name= name;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password= password;
    }
//
//    @Override
//    public String toString(){
//        return String.format("Question[No.=%d, Title='%s']", id, title);
//    }
}
