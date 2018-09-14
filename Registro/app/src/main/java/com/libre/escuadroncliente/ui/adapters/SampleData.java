package com.libre.escuadroncliente.ui.adapters;
import com.libre.escuadroncliente.ui.pojos.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static final int SAMPLE_DATA_ITEM_COUNT = 4;

    public static ArrayList<Product> generateSampleData(JSONArray jsonData) {
        final ArrayList<Product> data = new ArrayList<Product>(SAMPLE_DATA_ITEM_COUNT);
        try{
            int x=jsonData.length();
            for (int i = 0; i <=x-1; i++) {
                JSONObject productObject=jsonData.getJSONObject(i);

                Product product=new Product();
                product.id=productObject.getString("id");
                product.name=productObject.getString("desc");
                product.price=productObject.getString("precio");
                product.image=productObject.getString("image");
                data.add(product);
            }

        }catch (JSONException ex){
            ex.getStackTrace();
        }


        return data;
    }

}