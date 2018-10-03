package com.libre.escuadroncliente.ui;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.gjiazhe.scrollparallaximageview.ScrollParallaxImageView;
import com.gjiazhe.scrollparallaximageview.parallaxstyle.VerticalMovingStyle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.fragments.DetailFragment;
import com.libre.escuadroncliente.ui.fragments.DigitalCode;
import com.libre.escuadroncliente.ui.fragments.DigitalCodeRegister;
import com.libre.escuadroncliente.ui.fragments.MapFragment;
import com.libre.escuadroncliente.ui.fragments.PayFragment;
import com.libre.escuadroncliente.ui.fragments.SubListFragment;
import com.libre.escuadroncliente.ui.pojos.CartOrder;
import com.libre.escuadroncliente.ui.pojos.Order;
import com.libre.escuadroncliente.ui.pojos.Product;
import com.libre.escuadroncliente.ui.storage.PreferencesStorage;
import com.libre.escuadroncliente.ui.util.Data;
import com.michaldrabik.tapbarmenulib.TapBarMenu;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE;
import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE_CONFIG;
import static com.libre.escuadroncliente.ui.util.Constants.URL_REMOTE;
import static com.libre.escuadroncliente.ui.util.Data.saveJSONFile;

public class MarketActivity extends  Activity {



    private Context context;
    private Fragment detailFragment=new DetailFragment();
    private Fragment digitalCode=new DigitalCode();
    private Fragment subListFragment=new SubListFragment();
    private Fragment payFragment=new PayFragment();
    private Fragment mapFragment=new MapFragment();


    public List<CartOrder> productList;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private  RecyclerView recyclerView;
    private int count = 0;
    private  MenuItem menuItem;
    private String userGuid="afsdajksmsdansjdnak";
    private DatabaseReference mDatabase;
    private Calendar calendar ;
    private Date now ;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Uri imageUri;
    public Order order;
    private TapBarMenu tapBarMenu;
    private ImageView imgPhoto,imgMap,imgUpload,imgCode,imageToast;
    private FirebaseStorage storage;
    final long ONE_MEGABYTE = 1024 * 1024;
    private PreferencesStorage prefs;
    private  TextView textToas;
    private View layoutToast;
    private boolean isActive=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_activity);
         calendar = Calendar.getInstance();
        context=this;
        prefs=new PreferencesStorage(context);
        userGuid=prefs.loadData("REGISTER_USER_KEY");
        isActive=Boolean.parseBoolean(prefs.loadData("REGISTER_USER_ACTIVE"));
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        context = this;
        productList=new ArrayList<>();
        storage=FirebaseStorage.getInstance();
        recyclerView =findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
        LayoutInflater inflater = getLayoutInflater();
        layoutToast = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));


        textToas=  layoutToast.findViewById(R.id.textToas);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgPhoto= findViewById(R.id.imgPhoto);
        imgUpload= findViewById(R.id.imgUpload);
        imgCode= findViewById(R.id.imgCode);
        imgMap= findViewById(R.id.imgMap);
        tapBarMenu=findViewById(R.id.tapBarMenu);
        tapBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                tapBarMenu.toggle();
            }
        });

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", null );
                initTicketPhotoFragment(bundle);
            }
        });

        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMapFragment();
            }
        });
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPreviusOrder();


            }
        });
        imgCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("productItem", 10001 );
                initFragmentCode( bundle);
            }
        });

        order=new Order();
        order.id=1;
    }


    @Override
    public void onBackPressed(){

        if(isActive) {
            if (detailFragment.isVisible()) {
                getFragmentManager().beginTransaction().remove(detailFragment).commit();

            }
            if (mapFragment.isVisible()) {
                getFragmentManager().beginTransaction().remove(mapFragment).commit();

            }
            if (payFragment.isVisible()) {
                getFragmentManager().beginTransaction().remove(payFragment).commit();

            }
            if (subListFragment.isVisible() && !detailFragment.isVisible()) {
                getFragmentManager().beginTransaction().remove(subListFragment).commit();

            }
            if (!detailFragment.isVisible() &&
                    !subListFragment.isVisible() &&
                    !payFragment.isVisible() &&
                    !mapFragment.isVisible()) {
                finish();
            }
        }else{
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isActive){
            Bundle bundle = new Bundle();
            bundle.putInt("productItem", 10001 );
            initFragmentCode( bundle);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        Uri selectedImage = imageUri;
        getContentResolver().notifyChange(selectedImage, null);
        ContentResolver cr = getContentResolver();

        switch (requestCode) {
            case 200:
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        order.ticket= Data.bitmapToBase64(bitmap);
                        PayFragment payFragment=(PayFragment) fragmentManager.findFragmentByTag("PAY");
                        payFragment.setFrontImage();


                    } catch (Exception e) {

                        Log.e("Camera", e.toString());
                    }
                }
                break;

        }
    }


    public void addProduct(Product product){
        CartOrder cartOrder=new CartOrder();
        cartOrder.id=product.id;
        cartOrder.count=product.count;
        cartOrder.price=product.price;
        productList.add(cartOrder);
        count=totalProducto();


    }
    public void takePhotoTicket(){

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStorageDirectory(),  "Ticket.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, 200);
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
                    holder.iv.setImageResource(R.drawable.img_flores);
                break;
                case 1 : holder.iv.setImageResource(R.drawable.img_reposteria);
                    break;
                case 2 : holder.iv.setImageResource(R.drawable.img_extractos);
                    break;
                case 3 : holder.iv.setImageResource(R.drawable.img_unguentos);
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


    private void initTicketPhotoFragment( Bundle bundle){
        //  floatingPayButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        payFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, payFragment, "PAY");
        fragmentTransaction.commit();

    }
    private void initMapFragment( Bundle bundle){
        //  floatingPayButton.setVisibility(View.GONE);
        if(order.ticket==null){

            showError("Captura tu ticket de pago");
        }else {
            fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.container, mapFragment, "MAP");
            fragmentTransaction.commit();
        }

    }

    private void initFragmentDetail( Bundle bundle){
        //floatingPayButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        detailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, detailFragment, "Add detail");
        fragmentTransaction.commit();

    }
    private void initFragmentCode( Bundle bundle){
        // floatingPayButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        detailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, digitalCode, "Add detail");
        fragmentTransaction.commit();

    }
    private void initFragmentSubList( Bundle bundle){
        //  floatingPayButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        subListFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, subListFragment, "Add detail");
        fragmentTransaction.commit();

    }
    public void startDetailFragment(Product product){
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product );
        this.initFragmentDetail(bundle);
    }
    public void startMapFragment(){
        Bundle bundle = new Bundle();
        this.initMapFragment(bundle);
    }
    public void  closeCodeFragment(){

        if(isActive) {
            getFragmentManager().beginTransaction().remove(digitalCode).commit();
        }

    }
    public void  closeMapFragment(double latitude,double longitude){


        order.latitude=latitude;
        order.longitude=longitude;

        getFragmentManager().beginTransaction().remove(mapFragment).commit();

    }


    public int totalProducto(){
        int total=0;
        for (CartOrder product:productList  ) {
            total=product.count+total;
        }
        return total;
    }
    public int toPayProducto(){
        int totalPay=0;
        for (CartOrder product:productList  ) {
            int price=Integer.parseInt(product.price);
            totalPay=(totalPay+price)*product.count;
        }
        return totalPay;
    }

    public void checkPreviusOrder(){


        DatabaseReference reference = database.getReference("registro");
        Query query = reference.child("pedidos").child(userGuid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Order order_local = dataSnapshot.getValue(Order.class);
                    if(order_local.pay){
                        if (productList.size() > 0) {
                            if(order.ticket==null){
                                showError("Captura tu ticket de pago");
                                }else {
                                    if(order.latitude==0 && order.longitude==0){

                                        showError("Define tu lugar de entrega");
                                    }else {
                                        new RegisterOrderTask().execute();
                                    }
                                }
                        } else {
                            showError("Lista de Productos vacia");
                        }

                    }else{
                        showError("Tienes un pedido pendiente");
                    }
                }else{
                    new RegisterOrderTask().execute();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage().toString();
            }
        });

    }
    public void registerOrder(){

        DatabaseReference ref = database.getReference("registro");
        DatabaseReference usersRef = ref.child("pedidos");
        now = calendar.getTime();
        order.userGuid=userGuid;
        order.dateOrder=now.toString();
        order.pay=false;
        order.feedback=false;

        order.total=toPayProducto();
        order.productList=productList;
        usersRef.child(userGuid).setValue(order,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());

                } else {

                    prefs.saveDataObjet("QUIZ_PENDING",order.feedback);
                    order=null;
                    order=new Order();

                }
            }
        });


    }
    private class RegloadTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context, R.style.MyDialogTheme);
            dialog.setMessage("Actualizando articulos.");
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
                StorageReference fileRef = storage.getReferenceFromUrl(URL_REMOTE).child(JSON_FILE);
                if (fileRef != null) {
                    fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            saveJSONFile(bytes,"db");
                            downloadConfig();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                }


            }catch (InterruptedException ex){
                ex.getStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            dialog.dismiss();


        }
    }
    private class RegisterOrderTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context, R.style.MyDialogTheme);
            dialog.setMessage("Guardando pedido.");
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
                registerOrder();


            }catch (InterruptedException ex){
                ex.getStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            dialog.dismiss();
            order=new Order();
            productList=new ArrayList<>();
            toastFinish("Transaccion Completada");

        }
    }
    public void showError(String message){
        ViewDialog alert = new ViewDialog();
        alert.showDialog(MarketActivity.this, message);

    }
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("notifications")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new RegloadTask().execute();
        }
    };

    public void toastFinish(String message){
        textToas.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layoutToast);
        toast.show();
    }
    public void downloadConfig(){


        StorageReference fileRef = storage.getReferenceFromUrl(URL_REMOTE).child(JSON_FILE_CONFIG);
        if (fileRef != null) {

            fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try{

                        saveJSONFile(bytes, "config");
                        JSONObject dataObject = Data.loadJSONFileObjet("configuracion", "config");
                        JSONArray items = dataObject.getJSONArray("items");
                        JSONObject jsonObject = items.getJSONObject(0);
                        prefs.saveData("REGISTER_USER_ACTIVE", ""+jsonObject.getBoolean("activo"));

                    }catch (JSONException ex){
                        ex.getStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }

    }
}
