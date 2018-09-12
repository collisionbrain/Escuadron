package com.libre.escuadroncliente.ui.adapters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static final int SAMPLE_DATA_ITEM_COUNT = 5;

    public static ArrayList<String> generateSampleData(JSONArray jsonData) {
        final ArrayList<String> data = new ArrayList<String>(SAMPLE_DATA_ITEM_COUNT);
        try{
            int x=jsonData.length();
            for (int i = 0; i < x; i++) {
                JSONObject productObject=jsonData.getJSONObject(0);
                data.add(productObject.getString("desc"));
            }

        }catch (JSONException ex){}


        return data;
    }

}