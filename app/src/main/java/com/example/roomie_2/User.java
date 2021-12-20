package com.example.roomie_2;

public class User {

    public String fullName, age, email,id;

    public Boolean hasApartment;
    public Boolean isAdmin;

    public int apartmentId;

    public User(){

    }

    public User(String fullName , String age , String email,String id){
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.id = id;
        hasApartment = false;
        isAdmin = false;
    }

}
