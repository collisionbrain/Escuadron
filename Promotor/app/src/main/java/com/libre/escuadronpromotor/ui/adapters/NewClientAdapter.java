package com.libre.escuadronpromotor.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libre.escuadronpromotor.R;
import com.libre.escuadronpromotor.ui.pojos.Member;

import java.util.List;

/**
 * Created by hugo on 8/09/18.
 */

public class NewClientAdapter extends RecyclerView.Adapter<NewClientAdapter.MyViewHolder> {

    private Context context;
    private List<Member> memberList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView mail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.client_name);
            mail = view.findViewById(R.id.client_mail);
        }
    }


    public NewClientAdapter(Context context, List<Member> memberList) {
        this.context = context;
        this.memberList = memberList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Member member = memberList.get(position);

        holder.name.setText(member.name);
        holder.mail.setText(member.mail);


    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

}
