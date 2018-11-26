package com.libre.escuadronpromotor.ui;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.libre.escuadronpromotor.R;
import com.libre.escuadronpromotor.ui.adapters.NewClientAdapter;
import com.libre.escuadronpromotor.ui.adapters.NewOrderAdapter;
import com.libre.escuadronpromotor.ui.fragments.MapFragment;
import com.libre.escuadronpromotor.ui.pojos.Delivery;
import com.libre.escuadronpromotor.ui.pojos.Member;
import com.libre.escuadronpromotor.ui.pojos.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ListDeliveryActivity extends AppCompatActivity {



    private Context context;
    public List<Order> productOrder;
    private RecyclerView recyclerView;
    private int count = 0;
    private DatabaseReference mDatabase;
    private Date now ;
    private NewOrderAdapter newOrderAdapter;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String userGuid;
    public Dialog dialogError;
    private FragmentTransaction fragmentTransaction;
    private Fragment prev;
    private FragmentManager fragmentManager;
    private MapFragment mapFragment = new MapFragment();
    private List<Order> newOrders=new ArrayList<>();
    GenericTypeIndicator<Map<String, List<Order>>> genericTypeIndicator;
    private DatabaseReference ref;
    private List<Delivery> listOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.list_orders);
        context=this;
        productOrder=new ArrayList<>();
        recyclerView =findViewById(R.id.recycler_view_order);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
         listOrderPendings();



    }


    @Override
    public void onBackPressed(){


        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void  listOrderPendings(){
      final  DatabaseReference ref = database.getReference("registro");
      final  DatabaseReference pedRef = ref.child("pedidos");
      final  List<Delivery> orderDelivery=new ArrayList<>();
        Query query = pedRef.orderByChild("pay").equalTo(false);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot pedido: snapshot.getChildren()) {
                    final Order order=pedido.getValue(Order.class);
                    DatabaseReference cliRef = ref.child("clientes").child(order.userGuid);
                    Query queryCli = pedRef.orderByChild("pay").equalTo(false);

                    queryCli.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot cliente: dataSnapshot.getChildren()) {
                                Member member=cliente.getValue(Member.class);
                                Delivery delivery=new Delivery();
                                delivery.user_name=member.name;
                               /* delivery.mail=member.mail;
                                delivery.image=member.b64FrontId;
                                delivery.latitude=order.latitude;
                                delivery.longitude=order.longitude;
                                delivery.productList=order.productList;
                                */
                                orderDelivery.add(delivery);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

                newOrderAdapter = new NewOrderAdapter(context, orderDelivery);
                recyclerView.setAdapter(newOrderAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    public void startDetailOrder(Delivery order){
        initFragmentMapOrder(order);
    }

    private void initFragmentMapOrder(Delivery order){
        Bundle bundle=new Bundle();
        bundle.putSerializable("order",order);
        fragmentTransaction = fragmentManager.beginTransaction();
        mapFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, mapFragment, "Add detail");
        fragmentTransaction.commit();

    }
}
