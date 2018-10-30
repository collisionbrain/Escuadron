package com.libre.escuadronpromotor.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.libre.escuadronpromotor.R;
import com.libre.escuadronpromotor.ui.ListDeliveryActivity;
import com.libre.escuadronpromotor.ui.pojos.Delivery;
import com.libre.escuadronpromotor.ui.pojos.Member;
import com.libre.escuadronpromotor.ui.pojos.Order;
import com.libre.escuadronpromotor.ui.util.Data;

import java.util.List;

/**
 * Created by hugo on 8/09/18.
 */

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.MyViewHolder> {

    private Context context;
    private List<Delivery> orderList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mail;
        public TextView name;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            mail = view.findViewById(R.id.item_name);
            name = view.findViewById(R.id.item_email);
        }
    }


    public NewOrderAdapter(Context context, List<Delivery> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       final Delivery order = orderList.get(position);

        holder.name.setText(order.name);
        holder.mail.setText(order.mail);
        Bitmap bitmap= Data.base64ToBitmap(order.image);
        holder.imageView.setImageBitmap(bitmap);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListDeliveryActivity)context).startDetailOrder(order);
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

}
