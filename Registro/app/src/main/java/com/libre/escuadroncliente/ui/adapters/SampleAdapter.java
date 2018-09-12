package com.libre.escuadroncliente.ui.adapters;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightTextView;
import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.MarketActivity;

/***
 * ADAPTER
 */

public class SampleAdapter extends ArrayAdapter<String> {

    private static final String TAG = "SampleAdapter";

    static class ViewHolder {
        DynamicHeightTextView txtLineOne;
        Button btnPlus,btnMinus;
    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;
    private Context context;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public SampleAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        this.context=context;
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.green_toolbar);
        mBackgroundColors.add(R.color.green_toolbar);
        mBackgroundColors.add(R.color.green_toolbar);
        mBackgroundColors.add(R.color.green_toolbar);
        mBackgroundColors.add(R.color.green_toolbar);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne =  convertView.findViewById(R.id.txt_line1);
            vh.txtLineOne.setBackground(context.getDrawable(R.drawable.bud));
            vh.btnPlus = convertView.findViewById(R.id.btn_plus);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
        int backgroundIndex = position >= mBackgroundColors.size() ?
                position % mBackgroundColors.size() : position;

        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

        Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

        vh.txtLineOne.setHeightRatio(positionHeight);
        vh.txtLineOne.setText(getItem(position) + position);

        vh.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                ((MarketActivity)context).startDetailFragment(position);
            }
        });

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
}