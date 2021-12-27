package com.example.roomie_2;

import android.icu.number.Precision;

import java.text.DecimalFormat;

public class Bill {

    public String debtor;
    public String owesMoney;
    private double amount;
    private static DecimalFormat dp = new DecimalFormat(".#");

    public Bill(String debtor,String owesMoney,double amount){
        this.debtor = debtor;
        this.owesMoney = owesMoney;
        this.amount = amount ;

    }
    public Bill(){

    }

    public void add_to_amount(double anotherDebt){
        amount+=anotherDebt;
    }
    public void reduce_from_amount(double anotherDebt){
        amount-=anotherDebt;
    }

    public void precition(){
        amount = Double.valueOf(dp.format(amount));
    }

    public void init_amount(){
        amount=0;
    }


    public double getAmount() {
        return amount;
    }

}
