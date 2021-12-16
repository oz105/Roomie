package com.example.roomie_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Apartment {
    public String name,address,Password;
    public int apartmentId;
    public String adminId;
    public List<ShopItem> shoppingList;
    public List<Bill> billsList = new ArrayList<>();
    public Map<String,String> apartmentUsers = new HashMap<>();
    private static int[] apartmentIndex = new int[100];


    public Apartment(String name, String address, String password, String adminId){
        this.name=name;
        this.address=address;
        this.Password= password;
        this.adminId = adminId;
        apartmentId=get_apartment_id();
        shoppingList = new LinkedList<ShopItem>();
        shoppingList.add(new ShopItem("test Item",5.5f));
    }

    public void insert_name_and_id(String id,String name){
        apartmentUsers.put(id,name);
    }

    private int get_apartment_id(){
        for (int i = 20; i < apartmentIndex.length; i++) {
            if(apartmentIndex[i]==0){
                apartmentIndex[i]=1;
                return i;
            }
        }
        return -1;
    }








}
