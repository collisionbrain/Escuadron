package com.libre.registro.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.JsonObject;
import com.libre.registro.R;
import com.libre.registro.ui.adapters.SampleAdapter;
import com.libre.registro.ui.adapters.SampleData;
import com.libre.registro.ui.util.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubListFragment extends Fragment implements   AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

    private View view;
    private Context context;
    private int counter=0;
    private StaggeredGridView staggeredGridView;
    private SampleAdapter mAdapter;
    private ArrayList<String> mData;
    private int productRootItem;
    private  TextView txtHeaderTitle;
    private JSONArray data;
    private String title;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context = getActivity();
        this.view = inflater.inflate(R.layout.sublist_fragment, container, false);
        staggeredGridView=this.view.findViewById(R.id.grid_view_sublist);
        productRootItem =  getArguments().getInt("productRootItem");

        final LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        mAdapter=null;
         txtHeaderTitle = header.findViewById(R.id.txt_title);
         title=getTitle(productRootItem);
        data=Data.loadJSONFileObjet(title);
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                txtHeaderTitle.setText(title);

            }
        });
        staggeredGridView.addHeaderView(header);
        staggeredGridView.addFooterView(footer);
        if (mAdapter == null) {
            mAdapter = new SampleAdapter(getActivity(), R.id.txt_line1);
        }

        if (mData == null) {
            mData = SampleData.generateSampleData(data);
        }

        for (String data : mData) {
            mAdapter.add(data);
        }

        staggeredGridView.setAdapter(mAdapter);
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
                result= "Ungüentos";
                break;
            case 3:
                result= "Ungüentos";
                break;
            case 4:
                result= "Extractos";
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

    }
}
