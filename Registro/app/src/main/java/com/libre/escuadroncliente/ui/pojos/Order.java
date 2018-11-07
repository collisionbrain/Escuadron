package com.libre.escuadroncliente.ui.pojos;

import java.util.List;

public class Order {
    public int id;
    public String userGuid;
    public String  dateOrder;
    public double  total;
    public boolean pay;
    public String ticket;
    public boolean feedback;
    public double latitude;
    public double longitude;
    public List<CartOrder> productList;

}
