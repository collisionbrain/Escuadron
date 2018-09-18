package com.libre.escuadroncliente.ui.adapters;
import com.libre.escuadroncliente.ui.pojos.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static final int SAMPLE_DATA_ITEM_COUNT = 4;

    public static ArrayList<Product> generateSampleData(JSONObject jsonData) {
        final ArrayList<Product> data = new ArrayList<Product>(SAMPLE_DATA_ITEM_COUNT);
        try{
                JSONObject productObject=jsonData;


                JSONArray items= productObject.getJSONArray("items");

                for (int a=0;a<items.length()-1;a++) {
                    JSONObject jsonObject = items.getJSONObject(a);
                    Product product=new Product();
                    product.id=jsonObject.getString("id");
                    product.name=jsonObject.getString("desc");
                    product.price=jsonObject.getString("precio");
                    product.image=jsonObject.getString("image");
                    product.detalle= productObject.getString("detalle");
                    product.info_tecnica= productObject.getString("info_tecnica");
                    data.add(product);
                }



        }catch (JSONException ex){
            ex.getStackTrace();
        }


        return data;
    }

}