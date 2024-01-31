package com.example.snacz;

import java.util.ArrayList;
import java.util.List;

public class Order  {
    public static final int delivery =1;
    public static final int takeaway =2;
    private List<Item> items;
    private int orderType;

    public Order(){}
    public Order(int orderType){
        this.items=new ArrayList<>();
        this.orderType=orderType;
    }

    public void additem(Item item){
        this.items.add(item);
    }
    public void removeItem(Item item){
        this.items.remove(item);
    }
    public int getOrderType(){
        return this.orderType;
    }

    public int calculateTotal(){
        int total=0;
        for(Item item : items){
           total+=item.getPrice();
        }
        return total;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
