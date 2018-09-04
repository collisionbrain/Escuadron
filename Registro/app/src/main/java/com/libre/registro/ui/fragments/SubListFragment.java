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
import com.libre.registro.R;
import com.libre.registro.ui.adapters.SampleAdapter;
import com.libre.registro.ui.adapters.SampleData;

import java.util.ArrayList;

public class SubListFragment extends Fragment implements   AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

    private View view;
    private Context context;
    private int counter=0;
    private StaggeredGridView staggeredGridView;
    private SampleAdapter mAdapter;
    private ArrayList<String> mData;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        setRetainInstance(true);
        context = getActivity();
        this.view = inflater.inflate(R.layout.sublist_fragment, container, false);
        staggeredGridView=this.view.findViewById(R.id.grid_view_sublist);

            final LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        TextView txtHeaderTitle = header.findViewById(R.id.txt_title);
        TextView txtFooterTitle =  footer.findViewById(R.id.txt_title);
        txtHeaderTitle.setText("Flores del dia");

        staggeredGridView.addHeaderView(header);
        staggeredGridView.addFooterView(footer);
        if (mAdapter == null) {
            mAdapter = new SampleAdapter(getActivity(), R.id.txt_line1);
        }

        if (mData == null) {
            mData = SampleData.generateSampleData();
        }

        for (String data : mData) {
            mAdapter.add(data);
        }

        staggeredGridView.setAdapter(mAdapter);
        staggeredGridView.setOnScrollListener(this);
        staggeredGridView.setOnItemClickListener(this);
        return  this.view;
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
