package com.example.roomie_2;

public class User {

    public String fullName, age, email;
    public String id;
    private static int[] userIndex = new int[100];
    public Boolean hasApartment, manager;
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
        manager = false ;
        isAdmin = false;
    }

    private int get_user_id(){
        for (int i = 0; i < userIndex.length; i++) {
            if(userIndex[i]==0){
                userIndex[i]=1;
                return i;
            }
        }
        return -1;
    }



}
