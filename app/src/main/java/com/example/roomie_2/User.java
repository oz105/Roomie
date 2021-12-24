package com.example.roomie_2;

public class User {

    protected String fullName, age, email,id;
    protected Boolean hasApartment;
    protected Boolean isAdmin;
    protected long apartmentId;

    public User(){

    }

    public User(String fullName , String age , String email,String id,boolean isAdmin){
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.id = id;
        hasApartment = false;
        this.isAdmin = isAdmin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getHasApartment() {
        return hasApartment;
    }

    public void setHasApartment(Boolean hasApartment) {
        this.hasApartment = hasApartment;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }
}
