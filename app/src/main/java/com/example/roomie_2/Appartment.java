package com.example.roomie_2;

import java.util.ArrayList;
import java.util.List;

public class Appartment {
    String name;
    String address;
    String Password;
    int appartmentId;
    String adminId;
    List<String> appartmentUsers = new ArrayList<>();
    private static int[] appartmentIndex = new int[100];


    public Appartment(String name,String address, String password,String adminId){
        this.name=name;
        this.address=address;
        this.Password= password;
        this.adminId = adminId;
        appartmentUsers.add(adminId);
        appartmentId=get_appartment_id();
    }

    private int get_appartment_id(){
        for (int i = 0; i < appartmentIndex.length; i++) {
            if(appartmentIndex[i]==0){
                appartmentIndex[i]=1;
                return i;
            }
        }
        return -1;
    }








}
