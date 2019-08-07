package com.uzoebere.gasify.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("NewOrders")
public class NewOrders extends ParseObject {

    public NewOrders() {
        super();
    }

    public NewOrders(String orderId, Date date, String pName, double price){
        setOrderId(orderId);
        setOrderDate(date);
        setProductName(pName);
        setPrice(price);
    }

    public String getOrderId() {
        return getString("objectId");
    }

    public void setOrderId(String orderId) {
        put("objectId", orderId);
    }

    public Date getOrderDate() {
        return getDate("createdAt");
    }

    public void setOrderDate(Date orderDate) {
        put("createdAt", orderDate);
    }

    public String getProductName() {
        return getString("productName");
    }

    public void setProductName(String pName) {
        put("productName", pName);
    }

    public double getPrice() {
        return getDouble("totalPrice");
    }

    public void setPrice(double price) {
        put("totalPrice", price);
    }

    public double getQuantity() {
        return getDouble("qtyPerKg");
    }

    public void setQuantity(double qty) {
        put("qtyPerKg", qty);
    }

    public String getOrderStatus() {
        return getString("orderStatus");
    }

    public void setOrderStatus(String status) {
        put("orderStatus", status);
    }

    public void setRatings(float rate){
        put("ratings", rate);
    }

    public float getRatings(){
        return (float) get("ratings");
    }

    public void setImage(Products prodImg) {
        put("Image", prodImg);
    }

    public Products getImage()  {
        return (Products) getParseObject("Image");
    }
}
