package com.libre.escuadroncliente.ui;
import android.Manifest;
import android.animation.Animator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE;
import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE_CONFIG;
import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE_QUIZ;
import static com.libre.escuadroncliente.ui.util.Constants.URL_REMOTE;
import static com.libre.escuadroncliente.ui.util.Data.saveJSONFile;

public class MarketActivity extends  Activity {



    private Context context;
    private Fragment detailFragment=new DetailFragment();
    private Fragment digitalCode=new DigitalCode();
    private Fragment subListFragment;
    private Fragment payFragment=new PayFragment();
    private Fragment mapFragment=new MapFragment();
    private  PrettyDialog prettyDialog=null,prettyDialogError=null,prettyDialogPending=null;


    public List<CartOrder> productList;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private  RecyclerView recyclerView;
    private int count = 0;
    private String userGuid="afsdajksmsdansjdnak";
    private DatabaseReference mDatabase;
    private Calendar calendar ;
    private Date now ;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Uri imageUri;
    public Order order;
    private ImageView imgPhoto,imgMap,imgUpload,imgCode,imageToast;
    private FirebaseStorage storage;
    final long ONE_MEGABYTE = 1024 * 1024;
    private PreferencesStorage prefs;
    private View layoutToast;
    private boolean isActive=false;
    public boolean isDeliveryActive=false;
    public  static boolean isActivityOpen=true;
    public  boolean needUpdate;
    private ContentValues values;
    private TextView txtCount;

    private TextView txtHeader;
    private ProgressDialog checkDialog;
    private ImageView icon,imgStep1,imgStep2,imgStep3;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_activity);

        Intent intent = getIntent();
        needUpdate=intent.getBooleanExtra("UPDATE",false);
        Log.e("################","tttttttttt "+needUpdate);
        calendar = Calendar.getInstance();
        context=this;
        prettyDialog= new PrettyDialog(context);
        prettyDialogError= new PrettyDialog(context);
        prettyDialogPending= new PrettyDialog(context);
        prettyDialog.setIcon(
                R.drawable.pdlg_icon_info,     // icon resource
                R.color.pdlg_color_red,      // icon tint
                new PrettyDialogCallback() {   // icon OnClick listener
                    @Override
                    public void onClick() {
                        // Do what you gotta do
                    }
                })
                .addButton(
                        "OK",					// button text
                        R.color.pdlg_color_white,		// button text color
                        R.color.pdlg_color_green,		// button background color
                        new PrettyDialogCallback() {		// button OnClick listener
                            @Override
                            public void onClick() {

                                prettyDialog.dismiss();
                            }
                        }
                );
        prettyDialogError.setIcon(
                R.drawable.pdlg_icon_info,     // icon resource
                R.color.pdlg_color_red,      // icon tint
                new PrettyDialogCallback() {   // icon OnClick listener
                    @Override
                    public void onClick() {
                        // Do what you gotta do
                    }
                })
                .addButton(
                        "OK",					// button text
                        R.color.pdlg_color_white,		// button text color
                        R.color.pdlg_color_red,		// button background color
                        new PrettyDialogCallback() {		// button OnClick listener
                            @Override
                            public void onClick() {

                                prettyDialogError.dismiss();
                            }
                        }
                );
        prettyDialogPending.setIcon(
                R.drawable.pdlg_icon_info,     // icon resource
                R.color.pdlg_color_red,      // icon tint
                new PrettyDialogCallback() {   // icon OnClick listener
                    @Override
                    public void onClick() {
                        // Do what you gotta do
                    }
                })
                .addButton(
                        "Continuar pedido actual",					// button text
                        R.color.pdlg_color_white,		// button text color
                        R.color.pdlg_color_green,		// button background color
                        new PrettyDialogCallback() {		// button OnClick listener
                            @Override
                            public void onClick() {

                                prettyDialogPending.dismiss();
                            }
                        }
                ).addButton(
                "Cancelar pedido actual",					// button text
                R.color.pdlg_color_white,		// button text color
                R.color.pdlg_color_red,		// button background color
                new PrettyDialogCallback() {		// button OnClick listener
                    @Override
                    public void onClick() {

                        order=new Order();
                        order.id=0;
                        updateUICurrenteCart(order);
                        prettyDialogPending.dismiss();
                    }
                }
        );
        prefs=new PreferencesStorage(context);
        userGuid=prefs.loadData("REGISTER_USER_KEY");
        String status=prefs.loadData("REGISTER_USER_ACTIVE");
        String delivery=prefs.loadData("DELIVER_ACTIVE");
        isActive=Boolean.parseBoolean(status);
        isDeliveryActive=Boolean.parseBoolean(delivery);
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        context = this;
        productList=new ArrayList<>();
        storage=FirebaseStorage.getInstance();
        recyclerView =findViewById(R.id.recycler_view);
        txtCount=findViewById(R.id.txtCount);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
        LayoutInflater inflater = getLayoutInflater();
        layoutToast = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgPhoto= findViewById(R.id.imgPhoto);
        imgUpload= findViewById(R.id.imgUpload);
        imgCode= findViewById(R.id.imgCode);
        imgMap= findViewById(R.id.imgMap);
        imgStep1= findViewById(R.id.imgStep1);
        imgStep2= findViewById(R.id.imgStep2);
        imgStep3= findViewById(R.id.imgStep3);
        icon=findViewById(R.id.cart);
        txtHeader=findViewById(R.id.bar_title);
        if(isActive){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    icon.setVisibility(View.VISIBLE);
                }
            });

        }
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initTicketPhotoFragment();
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
                checkDialog = new ProgressDialog(context, R.style.MyDialogTheme);
                checkDialog.setMessage("Preparando pedido.");
                checkDialog.show();
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

        Order loadedOrder=(Order)prefs.loadDatObjet("CURRENT_CART");
        if(loadedOrder!=null){
            if(loadedOrder.id>0){
                order=loadedOrder;
                productList=order.productList;
                updateUICurrenteCart(order);
            }else{
                order=new Order();
                order.id=1;
            }
        }else{
            order=new Order();
            order.id=1;
        }



        if (checkPermissionACCESS_FINE_LOCATION(this)) {
            // do your stuff..
        }
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
                updateHeader("");
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
    public void onDestroy() {
        super.onDestroy();
        isActivityOpen=false;
        saveCurrentCart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCurrentCart();
    }
    @Override
    public void onResume() {
        super.onResume();


        if(!isActive){
            Bundle bundle = new Bundle();
            bundle.putInt("productItem", 10001 );
            initFragmentCode( bundle);
        }
        if(needUpdate){

          update();
        }
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;

        switch (requestCode) {
            case 200:
                if (resultCode == RESULT_OK) {
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), imageUri);
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
        txtCount.setVisibility(View.VISIBLE);
        txtCount.setText(""+count);


    }
    public void takePhotoTicket(){


        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Comprobante Donativo");
        values.put(MediaStore.Images.Media.DESCRIPTION, "None");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        this.startActivityForResult(intent, 200);
        Bitmap bitmap = ((BitmapDrawable)imgPhoto.getDrawable()).getBitmap();
        imgPhoto.setImageBitmap( changeBitmapColor(bitmap,Color.YELLOW));

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


    private void initTicketPhotoFragment(){
        if(totalProducto()>0) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, payFragment, "PAY");
            fragmentTransaction.commit();
        }else{
            prettyDialog.setTitle("")
                    .setMessage("No hay productos en la lista")

                    .show();
        }

    }
    private void initMapFragment(){
        //  floatingPayButton.setVisibility(View.GONE);
        if(order.ticket==null){

            showError("Captura tu ticket de pago");
        }else {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, mapFragment, "MAP");
            fragmentTransaction.commit();
        }

    }

    private void initFragmentDetail( Bundle bundle){
        //floatingPayButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        detailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, detailFragment, "Detail");
        fragmentTransaction.commit();

    }
    private void initFragmentCode( Bundle bundle){
        // floatingPayButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        detailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, digitalCode, "Code");
        fragmentTransaction.commit();

    }
    private void initFragmentSubList( Bundle bundle){
        //  floatingPayButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        subListFragment=new SubListFragment();
        subListFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, subListFragment, "Sublist");
        fragmentTransaction.commit();

    }
    public void startDetailFragment(Product product){
       Bundle  bundle = new Bundle();
        bundle.putSerializable("product", product );
        this.initFragmentDetail(bundle);
    }
    public void startMapFragment(){

        this.initMapFragment();
    }
    public void  closeCodeFragment(){

        if(isActive) {
            getFragmentManager().beginTransaction().remove(digitalCode).commit();
        }

    }
    public void  closeMapFragment(){


        Bitmap bitmap = ((BitmapDrawable)imgMap.getDrawable()).getBitmap();
        imgMap.setImageBitmap( changeBitmapColor(bitmap,Color.YELLOW));
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

        if (productList.size() > 0) {
            if(order.ticket==null){
                checkDialog.dismiss();
                showError("Captura tu ticket de pago");
            }else {
                if(order.latitude==0 && order.longitude==0){
                    checkDialog.dismiss();
                    showError("Define tu lugar de entrega");
                }else {
                    DatabaseReference reference = database.getReference("registro");
                    Query query = reference.child("pedidos").child(userGuid);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Order order_local = dataSnapshot.getValue(Order.class);
                                if(order_local.pay){
                                    checkDialog.dismiss();
                                    new RegisterOrderTask().execute();
                                }else{
                                    checkDialog.dismiss();
                                    showErrorPending("Tienes un pedido pendiente");
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
            }
        } else {
            checkDialog.dismiss();
            showError("Lista de Productos vacia");
        }

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
                    prefs.saveData("CURRENT_COUNT","");
                    order=null;
                    order=new Order();
                    prefs.saveDataObjet("CURRENT_CART",order);

                }
            }
        });


    }
    private class RegloadTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context, R.style.MyDialogTheme);
            dialog.setMessage("Buscando actualizaciones.");
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
                            Intent registerIntent = new Intent(context,Splash.class);
                            startActivity(registerIntent);
                            finish();

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
                downloadQuiz();


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
            prettyDialog.setTitle(":)")
                    .setMessage("Pedido realizado")
                    .show();
            Bitmap bitmap = ((BitmapDrawable)imgUpload.getDrawable()).getBitmap();
            imgUpload.setImageBitmap( changeBitmapColor(bitmap,Color.YELLOW));
            updateUICurrenteCart(order);
        }
    }

    private class RegisterCheckOrderTask extends AsyncTask<Void, Void, Integer> {
        private ProgressDialog dialog;
        private   int result;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context, R.style.MyDialogTheme);
            dialog.setMessage("Guardando pedido.");
            dialog.show();
        }
        @Override
        protected Integer doInBackground(Void... params) {
                result=0;
            if (productList.size() > 0) {
                if(order.ticket==null){
                    return 5;
                }else {
                    if(order.latitude==0 && order.longitude==0){

                        return 4;
                    }else {
                        DatabaseReference reference = database.getReference("registro");
                        Query query = reference.child("pedidos").child(userGuid);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Order order_local = dataSnapshot.getValue(Order.class);
                                    if(order_local.pay){

                                        result= 0;
                                    }else{
                                        result= 3;
                                    }
                                }else{
                                    result=0;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                databaseError.getMessage().toString();
                                result=0;
                            }
                        });

                    }
                }
            } else {
               return 2;
            }

            return  result;
        }

        @Override
        protected void onPostExecute(Integer param) {

            Log.e("XXXXXXXXXXXX",": "+param);
        }
    }

    public void showError(String message){
        prettyDialogError.setTitle("")
                .setMessage(message)
                .show();

    }
    public void showErrorPending(String message){
        prettyDialogPending.setTitle("")
                .setMessage(message)
                .show();

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
           update();
        }
    };

    public void toastFinish(String message){

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
                        boolean status=jsonObject.getBoolean("activo");
                        boolean delivery=jsonObject.getBoolean("delivery");
                        JSONArray pay = jsonObject.getJSONArray("pay");
                        List<String> account=new ArrayList<>();
                        for (int a=0;a<=pay.length()-1;a++) {
                            JSONObject jsonObjectAccount = pay.getJSONObject(a);
                            account.add(jsonObjectAccount.get("banco")+","+jsonObjectAccount.get("tarjeta"));
                        }



                        prefs.saveData("REGISTER_USER_ACTIVE", ""+status);
                        prefs.saveData("DELIVER_ACTIVE", ""+delivery);
                        prefs.saveData("PAY_ACCOUNT_ONE", account.get(0));
                        prefs.saveData("PAY_ACCOUNT_TWO", account.get(1));
                        prefs.saveData("PAY_ACCOUNT_THREE", account.get(2));
                        prefs.saveData("PAY_ACCOUNT_FOUR", account.get(3));

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
    public void downloadQuiz(){


        StorageReference fileRef = storage.getReferenceFromUrl(URL_REMOTE).child(JSON_FILE_QUIZ);
        if (fileRef != null) {

            fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    saveJSONFile(bytes, "quiz");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }

    }

    public boolean checkPermissionACCESS_FINE_LOCATION(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (MarketActivity) context,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    showDialog("Geolocalizacion ", context, Manifest.permission.ACCESS_FINE_LOCATION);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public void update(){
        new RegloadTask().execute();
    }

    public static Bitmap changeBitmapColor(Bitmap sourceBitmap, int color)
    {
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(),true);
        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        paint.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        return resultBitmap;
    }
    public static Bitmap returnBitmapColor(Bitmap sourceBitmap, int color)
    {
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(),true);
        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(color, -1);
        paint.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        return resultBitmap;
    }

    private  void saveCurrentCart(){
        now = calendar.getTime();
        order.userGuid=userGuid;
        order.dateOrder=now.toString();
        order.pay=false;
        order.feedback=false;
        order.total=toPayProducto();
        order.productList=productList;
        prefs.saveDataObjet("CURRENT_CART",order);
    }

    public void updateUICurrenteCart(Order order){
        final Bitmap ticket = ((BitmapDrawable)imgPhoto.getDrawable()).getBitmap();
        final Bitmap map = ((BitmapDrawable)imgMap.getDrawable()).getBitmap();
        final Bitmap upload = ((BitmapDrawable)imgUpload.getDrawable()).getBitmap();



        if(order.id>0){
            if(order.ticket!=null){

                imgPhoto.setImageBitmap( changeBitmapColor(ticket,Color.YELLOW));
            }else{

                imgPhoto.setImageBitmap( changeBitmapColor(ticket,Color.WHITE));
            }

            if(order.total!=0){
                txtCount.setVisibility(View.VISIBLE);
                txtCount.setText(""+totalProducto());
            }
            if(order.latitude!=0){
                 imgMap.setImageBitmap( changeBitmapColor(map,Color.YELLOW));
            }else{
                imgMap.setImageBitmap( changeBitmapColor(map,Color.WHITE));
            }

        }else{
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtCount.setVisibility(View.INVISIBLE);
                    imgPhoto.setImageBitmap(returnBitmapColor(ticket,Color.WHITE));
                    imgMap.setImageBitmap( returnBitmapColor(map,Color.WHITE));
                    imgUpload.setImageBitmap( returnBitmapColor(upload,Color.WHITE));
                }
            });

        }
        stepsViews(order);
    }
    public void stepsViews(final Order order){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(order.productList.size()>0){

                    if(order.ticket==null){
                        imgStep1.setVisibility(View.VISIBLE);
                        imgStep1.animate()
                                .translationX(100)
                                .translationY(imgStep1.getHeight())
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .setDuration(500)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                    }else{
                        imgStep1.setVisibility(View.INVISIBLE);
                        if(order.latitude==0 && order.longitude==0){
                            imgStep2.setVisibility(View.VISIBLE);
                        }else{
                            imgStep2.setVisibility(View.INVISIBLE);
                        }
                    }

                }



            }
        });
    }
    public void updateHeader(final String title){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtHeader.setText(title);

            }
        });
    }
}
