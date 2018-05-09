package com.example.waithera.nannyalert;

/**
 * Created by Waithera on 11/04/2018.
 */

public class Nanny {
    private String name,desc,image,username,obNumber;

    public Nanny(){

    }
    public Nanny(String name,String desc,String image,String username,String obNumber){
        this.name=name;
        this.desc=desc;
        this.image=image;
        this.username=username;
        this.obNumber=obNumber;
    }

    public String getName() {
        return name;
    }

    public String getObNumber() {
        return obNumber;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setObNumber(String obNumber) {
        this.obNumber = obNumber;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUsername(String username) {
        this.username = username;
    }




}
