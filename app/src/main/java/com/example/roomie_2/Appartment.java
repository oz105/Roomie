package com.example.roomie_2;

public class Appartment {
    String name;
    String address;
    String Password;
    int appartmentId;
    static int APARTMENT_ID = 0;
    int adminId;

    public Appartment(String name,String address, String password,int adminId){
        this.name=name;
        this.address=address;
        this.Password= password;
        this.adminId = adminId;
        appartmentId=APARTMENT_ID++;
    }








}
