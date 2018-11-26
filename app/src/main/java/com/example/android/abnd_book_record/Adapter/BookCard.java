package com.example.android.abnd_book_record.Adapter;

/**
 * Created by Akanksha_Rajwar on 26-11-2018.
 */

public class BookCard {
    private String name;
    private int price;
    private int quantity;

    public BookCard(String Sentname, int Sentprice, int Sentquantity) {
        name=Sentname;
        price=Sentprice;
        quantity=Sentquantity;


    }

    public String getProdName()
    {
        return name;
    }
    public int getPrice()
    {
        return price;

    }

    public int getQuantity()
    {
        return quantity;
    }


}