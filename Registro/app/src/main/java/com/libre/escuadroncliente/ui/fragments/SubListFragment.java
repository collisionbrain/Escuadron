package com.libre.escuadroncliente.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.JsonObject;
import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.MarketActivity;
import com.libre.escuadroncliente.ui.adapters.SampleAdapter;
import com.libre.escuadroncliente.ui.adapters.SampleData;
import com.libre.escuadroncliente.ui.pojos.Product;
import com.libre.escuadroncliente.ui.util.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubListFragment extends Fragment implements   AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

    private View view;
    private Context context;
    private int counter=0;
    private GridView staggeredGridView;
    private SampleAdapter mAdapter;
    private ArrayList<Product> mData;
    private int productRootItem;
    private  TextView txtHeaderTitle;
    private JSONObject data;
    private String title;
    private  View header,footer;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context = getActivity();
        this.view = inflater.inflate(R.layout.gridview_fragment, container, false);
        staggeredGridView=this.view.findViewById(R.id.grid_view_sublist);
        productRootItem =  getArguments().getInt("productRootItem");

        final LayoutInflater layoutInflater = getActivity().getLayoutInflater();
         header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
         footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
         mAdapter=null;
         txtHeaderTitle = header.findViewById(R.id.txt_title);
         title=getTitle(productRootItem);
        data=Data.loadJSONFileObjet(title,"db");
        ((MarketActivity)context).updateHeader(title);
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                //txtHeaderTitle.setText(title);
               // staggeredGridView.addHeaderView(header);
               // staggeredGridView.addFooterView(footer);
                mAdapter = new SampleAdapter(getActivity(), R.id.txt_line1);
                mData = SampleData.generateSampleData(data);


                for (Product data : mData) {

                    mAdapter.add(data);
                }

                staggeredGridView.setAdapter(mAdapter);
            }
        });


        staggeredGridView.setOnScrollListener(this);
        staggeredGridView.setOnItemClickListener(this);
        return  this.view;
    }


    public String getTitle(int position){
        String result="";
        switch (position){
            case 0:
                result= "Flores";
                break;
            case 1:
                result= "Reposteria";
                break;
            case 2:
                result= "Extractos";
                break;
            case 3:
                result= "Ungüentos";
                break;

        }

        return result;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.e("###########","position : "+position);
        Log.e("###########","id : "+id);

    }
}
