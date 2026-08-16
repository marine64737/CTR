package com.shkim.CTR.Domain.User.DTO;

import java.io.Serializable;

public class UserDTO implements Serializable {

    private int id;

    private String name;

    private int platform;

    public UserDTO(int id, String name, int platform) {
        this.id = id;
        this.name = name;
        this.platform = platform;
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

    public void setName(String username){
        this.name= name;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }
}
