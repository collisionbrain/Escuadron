package com.libre.escuadronpromotor.ui;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.libre.escuadronpromotor.R;
import com.libre.escuadronpromotor.ui.pojos.Product;
import com.libre.escuadronpromotor.ui.storage.PreferencesStorage;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListUsersActivity extends AppCompatActivity {



    private Context context;
    public List<Product> productList;
    private RecyclerView recyclerView;
    private int count = 0;
    private String userGuid="";
    private DatabaseReference mDatabase;
    private Calendar calendar ;
    private Date now ;
    private Button floatingActionButton,floatingSyncButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_users);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fff5f5f5")));
        calendar = Calendar.getInstance();
        context=this;
        PreferencesStorage prefs=new PreferencesStorage(context);
        userGuid=prefs.loadData("REGISTER_USER_KEY");
        context = this;
        productList=new ArrayList<>();
        recyclerView =findViewById(R.id.recycler_view);
        //recyclerView.setAdapter(new MyAdapter());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        floatingActionButton= findViewById(R.id.action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        floatingSyncButton= findViewById(R.id.sync_button);
        floatingSyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ListUsersActivity.this,RegisterActivity.class);
                startActivityForResult(intent,500);
            }
        });
    }


    @Override
    public void onBackPressed(){


        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 500) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

            }
        }
    }




}
