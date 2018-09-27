package com.libre.escuadronpromotor.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libre.escuadronpromotor.R;
import com.libre.escuadronpromotor.ui.ListDeliveryActivity;
import com.libre.escuadronpromotor.ui.pojos.Member;
import com.libre.escuadronpromotor.ui.pojos.Order;

import java.util.List;

/**
 * Created by hugo on 8/09/18.
 */

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.MyViewHolder> {

    private Context context;
    private List<Order> orderList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dateOrder;
        public TextView mail;

        public MyViewHolder(View view) {
            super(view);
            dateOrder = view.findViewById(R.id.client_name);
            mail = view.findViewById(R.id.client_mail);
        }
    }


    public NewOrderAdapter(Context context, List<Order> orderList) {
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
       final Order order = orderList.get(position);

        holder.dateOrder.setText(order.dateOrder);
        holder.dateOrder.setOnClickListener(new View.OnClickListener() {
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
