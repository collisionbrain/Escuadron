package com.libre.escuadronpromotor.ui.pojos;

import java.io.Serializable;
import java.util.List;

public class Delivery implements Serializable {

    public String userGuid;
    public String name;
    public String mail;
    public String image;
    public String  date;
    public double  total;
    public boolean pay;
    public String ticket;
    public double latitude;
    public double longitude;
    public List<CartOrder> productList;
}
