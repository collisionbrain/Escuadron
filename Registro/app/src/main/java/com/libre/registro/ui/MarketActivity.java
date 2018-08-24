package com.libre.registro.ui;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.gjiazhe.scrollparallaximageview.ScrollParallaxImageView;
import com.gjiazhe.scrollparallaximageview.parallaxstyle.VerticalMovingStyle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.libre.registro.R;
import com.libre.registro.ui.fragments.DetailFragment;
import com.libre.registro.ui.pojos.Order;
import com.libre.registro.ui.pojos.Product;
import com.libre.registro.ui.storage.PreferencesStorage;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MarketActivity extends AppCompatActivity{



    private Context context;
    private Fragment detailFragment=new DetailFragment();
    public List<Product> productList;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private  RecyclerView recyclerView;
    private int count = 0;
    private  MenuItem menuItem;
    private String userGuid;
    private DatabaseReference mDatabase;
    private Calendar calendar ;
    private Date now ;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_activity);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);
        calendar = Calendar.getInstance();
        PreferencesStorage prefs=new PreferencesStorage(context);
        userGuid=prefs.loadData("REGISTER_USER_KEY");
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        context = this;
        productList=new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userGuid);
        floatingActionButton=(FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public void onBackPressed(){

        getFragmentManager().beginTransaction().remove(detailFragment).commit();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menuItem = menu.findItem(R.id.testAction);
        menuItem.setIcon(buildCounterDrawable(count,  R.drawable.cart));

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void addProduct(Product product){
        productList.add(product);
        count=productList.size();
        menuItem.setIcon(buildCounterDrawable(count,  R.drawable.cart));
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private VerticalMovingStyle verticalMovingStyle = new VerticalMovingStyle();
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = recyclerView.indexOfChild(v);
                    Log.e("xxxxxxxxxx",""+itemPosition);
                    Bundle bundle = new Bundle();
                    bundle.putInt("productItem", itemPosition );
                    initFragment( bundle);
                }
            });
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.iv.setParallaxStyles(verticalMovingStyle);
            switch (position % 5) {
                case 0 : holder.iv.setImageResource(R.drawable.cbd); break;
                case 1 : holder.iv.setImageResource(R.drawable.muffin); break;
                case 2 : holder.iv.setImageResource(R.drawable.cookies); break;
                case 3 : holder.iv.setImageResource(R.drawable.bud); break;
                case 4 : holder.iv.setImageResource(R.drawable.pomada); break;
            }
        }

        @Override
        public int getItemCount() {
            return 8;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ScrollParallaxImageView iv;
            ViewHolder(final View itemView) {
                super(itemView);
                iv = (ScrollParallaxImageView) itemView.findViewById(R.id.img);

            }
        }
    }

    private void initFragment( Bundle bundle){
        fragmentTransaction = fragmentManager.beginTransaction();
        detailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, detailFragment, "Add detail");
        fragmentTransaction.commit();

    }
    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
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