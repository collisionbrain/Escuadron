package com.libre.escuadroncliente.ui;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.gjiazhe.scrollparallaximageview.ScrollParallaxImageView;
import com.gjiazhe.scrollparallaximageview.parallaxstyle.VerticalMovingStyle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.fragments.DetailFragment;
import com.libre.escuadroncliente.ui.fragments.DigitalCode;
import com.libre.escuadroncliente.ui.fragments.DigitalCodeRegister;
import com.libre.escuadroncliente.ui.fragments.SubListFragment;
import com.libre.escuadroncliente.ui.pojos.Order;
import com.libre.escuadroncliente.ui.pojos.Product;
import com.libre.escuadroncliente.ui.storage.PreferencesStorage;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MarketActivity extends AppCompatActivity   {



    private Context context;
    private Fragment detailFragment=new DetailFragment();
    private Fragment digitalCode=new DigitalCode();
    private Fragment subListFragment=new SubListFragment();
    public List<Product> productList;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private  RecyclerView recyclerView;
    private int count = 0;
    private  MenuItem menuItem;
    private String userGuid="afsdajksmsdansjdnak";
    private DatabaseReference mDatabase;
    private Calendar calendar ;
    private Date now ;
    private Button floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_activity);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#fff5f5f5")));
        calendar = Calendar.getInstance();
        context=this;
        PreferencesStorage prefs=new PreferencesStorage(context);
        userGuid=prefs.loadData("REGISTER_USER_KEY");
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        context = this;
        productList=new ArrayList<>();
        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        floatingActionButton= findViewById(R.id.action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("productItem", 10001 );
                initFragmentCode( bundle);
            }
        });


    }


    @Override
    public void onBackPressed(){


        if(detailFragment.isVisible()){
            getFragmentManager().beginTransaction().remove(detailFragment).commit();
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        if(subListFragment.isVisible() && !detailFragment.isVisible()){
            getFragmentManager().beginTransaction().remove(subListFragment).commit();
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        if(!detailFragment.isVisible() && !subListFragment.isVisible()){
           finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Bundle bundle = new Bundle();
            bundle.putInt("productItem", 10001 );
            initFragmentCode( bundle);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void addProduct(Product product){
        productList.add(product);
        count=productList.size();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                floatingActionButton.setText("Comprar :" +count);
            }
        });

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private VerticalMovingStyle verticalMovingStyle = new VerticalMovingStyle();
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.iv.setParallaxStyles(verticalMovingStyle);
            switch (position % 5) {
                case 0 :
                    holder.iv.setImageResource(R.drawable.bud);
                break;

                case 1 : holder.iv.setImageResource(R.drawable.cookies);
                    break;
                case 2 : holder.iv.setImageResource(R.drawable.cbd);
                    break;
                case 3 : holder.iv.setImageResource(R.drawable.pomada);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ScrollParallaxImageView iv;
            ViewHolder(final View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.img);
                iv.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                int itemPosition=getLayoutPosition();
                Log.e("#######",""+ itemPosition);
                Bundle bundle = new Bundle();
                bundle.putInt("productRootItem", itemPosition );
                initFragmentSubList(bundle);

            }
        }
    }

    private void initFragmentDetail( Bundle bundle){
        floatingActionButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        detailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, detailFragment, "Add detail");
        fragmentTransaction.commit();

    }
    private void initFragmentCode( Bundle bundle){
        floatingActionButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        detailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, digitalCode, "Add detail");
        fragmentTransaction.commit();

    }
    private void initFragmentSubList( Bundle bundle){
        floatingActionButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        subListFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, subListFragment, "Add detail");
        fragmentTransaction.commit();

    }
    public void startDetailFragment(int pos){
        Bundle bundle = new Bundle();
        bundle.putInt("productItem", pos );
        this.initFragmentDetail(bundle);
    }
    public void  closeCodeFragment(){

        getFragmentManager().beginTransaction().remove(digitalCode).commit();
        floatingActionButton.setVisibility(View.VISIBLE);
    }


    public void registerOrder(){
        now = calendar.getTime();
        Order order=new Order();
        order.userGuid=userGuid;
        order.dateOrder=now;
        mDatabase.child("users").child(userGuid).setValue(order,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {


                } else {



                }
            }
        });
    }
}
