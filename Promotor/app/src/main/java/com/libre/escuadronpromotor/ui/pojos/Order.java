package com.libre.escuadronpromotor.ui.pojos;

import java.io.Serializable;
import java.util.List;

public class Order  implements Serializable{
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
