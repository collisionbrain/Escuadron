package com.libre.escuadroncliente.ui.pojos;

import java.util.List;

public class Order {
    public String userGuid;
    public String  dateOrder;
    public double  total;
    public boolean pay;
    public String ticket;
    public List<Product> productList;
}
