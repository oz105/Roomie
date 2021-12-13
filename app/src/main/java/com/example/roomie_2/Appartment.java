package com.example.roomie_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Appartment {
    public String name,address,Password;
    public int appartmentId;
    public String adminId;
    public Map<String,Float> shoppingList;
    public List<Bill> billsList = new ArrayList<>();
    public Map<String,String> appartmentUsers = new HashMap<>();
    private static int[] appartmentIndex = new int[100];


    public Appartment(String name,String address, String password,String adminId){
        this.name=name;
        this.address=address;
        this.Password= password;
        this.adminId = adminId;
        appartmentId=get_appartment_id();
        shoppingList = new HashMap<String,Float>();
        shoppingList.put("Bannana",5.5f);
    }

    public void insert_name_and_id(String id,String name){
        appartmentUsers.put(id,name);
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
