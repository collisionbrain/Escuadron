package com.libre.escuadroncliente.ui.adapters;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightTextView;
import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.MarketActivity;
import com.libre.escuadroncliente.ui.pojos.Product;
import com.libre.escuadroncliente.ui.util.Data;

import okhttp3.internal.Util;

/***
 * ADAPTER
 */

public class SampleAdapter extends ArrayAdapter<Product> {

    private static final String TAG = "SampleAdapter";

    static class ViewHolder {
        TextView txtLineOne;
        TextView txtLineTwo,txtLineThree;
        ImageView imgPhoto;
        RatingBar ratingBar;
    }

    private final LayoutInflater mLayoutInflater;
    private Context context;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public SampleAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        final Product product;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.grid_item, parent, false);
            vh = new ViewHolder();

            vh.txtLineOne =  convertView.findViewById(R.id.txtName);
            vh.txtLineTwo =  convertView.findViewById(R.id.txtPercentSative);
            vh.txtLineThree =  convertView.findViewById(R.id.txtPercentIndic);
            vh.imgPhoto=  convertView.findViewById(R.id.imgItem);
            vh.ratingBar=  convertView.findViewById(R.id.ratingProduct);
            convertView.setTag(vh);

        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        product=getItem(position);
        vh.txtLineOne.setText(product.name);
        vh.txtLineTwo.setText(product.flavor);
        vh.txtLineThree.setText(product.amount);
        if(product.image.length()>0) {
            vh.imgPhoto.setImageBitmap(Data.base64ToBitmap(product.image));
        }
        vh.ratingBar.setRating(product.calification);
        vh.txtLineOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                ((MarketActivity)context).startDetailFragment(product);
            }
        });

        return convertView;
    }


}