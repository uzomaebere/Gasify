package com.uzoebere.gasify.models;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseClassName;

@ParseClassName("Products")
public class Products extends ParseObject{

    public Products() {
        super();
    }

    public Products(String pName, String pType, String size, String color, String desc, ParseFile parseFile) {
        super();
        setProductName(pName);
        setProductType(pType);
        setImage(parseFile);
        setDesc(desc);
    }

    public String getProductName() {
        return getString("ProductName");
    }

    public void setProductName(String pName) {
        put("ProductName", pName);
    }

    public void setProductType(String pType) {
        put("productType", pType);
    }

    public void setDesc(String desc) {
        put("productDescription", desc);
    }

    public int getPrice() {
        return getInt("price");
    }

    public void setPrice(int price) {
        put("price", price);
    }

    public ParseFile getImage() {
        return getParseFile("Image");
    }

    public void setImage(ParseFile parseFile) {
        put("Image", parseFile);
    }
}
