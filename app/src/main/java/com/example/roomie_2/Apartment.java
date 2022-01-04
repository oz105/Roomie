package com.example.roomie_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Bills.Bill;
import ShoppingList.ShopItem;

public class Apartment {
    private Map<String,Object> details;
    private long apartmentId;
    protected String adminId;
    protected List<ShopItem> shoppingList;
    protected List<Bill> billsList = new ArrayList<>();
    protected Map<String,String> apartmentUsers = new HashMap<>();


    public Apartment(Map<String,Object> details,long apartmentId){

        this.details = details;
        this.apartmentId=apartmentId;
        shoppingList = new LinkedList<ShopItem>();
    }

    public void insert_name_and_id(String id,String name){
        apartmentUsers.put(id,name);
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public List<ShopItem> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<ShopItem> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public List<Bill> getBillsList() {
        return billsList;
    }

    public void setBillsList(List<Bill> billsList) {
        this.billsList = billsList;
    }

    public Map<String, String> getApartmentUsers() {
        return apartmentUsers;
    }

    public void setApartmentUsers(Map<String, String> apartmentUsers) {
        this.apartmentUsers = apartmentUsers;
    }
}
