package com.libre.escuadroncliente.ui;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.fragments.DetailFragment;
import com.libre.escuadroncliente.ui.fragments.DigitalCode;
import com.libre.escuadroncliente.ui.fragments.DigitalCodeRegister;
import com.libre.escuadroncliente.ui.fragments.PayFragment;
import com.libre.escuadroncliente.ui.fragments.SubListFragment;
import com.libre.escuadroncliente.ui.pojos.Order;
import com.libre.escuadroncliente.ui.pojos.Product;
import com.libre.escuadroncliente.ui.storage.PreferencesStorage;
import com.libre.escuadroncliente.ui.util.Data;
import com.michaldrabik.tapbarmenulib.TapBarMenu;

import android.util.Log;
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
import android.widget.Toolbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE;
import static com.libre.escuadroncliente.ui.util.Constants.URL_REMOTE;
import static com.libre.escuadroncliente.ui.util.Data.saveJSONFile;

public class MarketActivity extends  Activity {



    private Context context;
    private Fragment detailFragment=new DetailFragment();
    private Fragment digitalCode=new DigitalCode();
    private Fragment subListFragment=new SubListFragment();
    private Fragment payFragment=new PayFragment();
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
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Uri imageUri;
    public Order order;
    private TapBarMenu tapBarMenu;
    private ImageView imgPhoto,imgReload,imgUpload,imgCode;
    private FirebaseStorage storage;
    final long ONE_MEGABYTE = 1024 * 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_activity);
         calendar = Calendar.getInstance();
        context=this;
        PreferencesStorage prefs=new PreferencesStorage(context);
        userGuid=prefs.loadData("REGISTER_USER_KEY");
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        context = this;
        productList=new ArrayList<>();
        storage=FirebaseStorage.getInstance();
        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgPhoto= findViewById(R.id.imgPhoto);
        imgUpload= findViewById(R.id.imgUpload);
        imgCode= findViewById(R.id.imgCode);
        imgReload= findViewById(R.id.imgReload);
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
        imgReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegloadTask().execute();
            }
        });
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(productList.size()>0) {
                    new RegisterOrderTask().execute();
                }else{
                    showError("Lista de Productos vacia");
                }

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


        if(detailFragment.isVisible()){
            getFragmentManager().beginTransaction().remove(detailFragment).commit();

        }
        if(payFragment.isVisible()){
            getFragmentManager().beginTransaction().remove(payFragment).commit();

        }
        if(subListFragment.isVisible() && !detailFragment.isVisible()){
            getFragmentManager().beginTransaction().remove(subListFragment).commit();

        }
        if(!detailFragment.isVisible() && !subListFragment.isVisible()&& !payFragment.isVisible()){
           finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
        product.image=null;
        productList.add(product);
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


    private void initTicketPhotoFragment( Bundle bundle){
        //  floatingPayButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        payFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, payFragment, "PAY");
        fragmentTransaction.commit();

    }
    private void initMapFragment( Bundle bundle){
        //  floatingPayButton.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        payFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, payFragment, "MAP");
        fragmentTransaction.commit();

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
    public void  closeCodeFragment(){

        getFragmentManager().beginTransaction().remove(digitalCode).commit();

    }


    public int totalProducto(){
        int total=0;
        for (Product product:productList  ) {
            total=product.count+total;
        }
        return total;
    }
    public int toPayProducto(){
        int totalPay=0;
        for (Product product:productList  ) {
            int price=Integer.parseInt(product.price);
            totalPay=totalPay+price;
        }
        return totalPay;
    }

    public void registerOrder(){

        DatabaseReference ref = database.getReference("registro");
        DatabaseReference usersRef = ref.child("pedidos");
        now = calendar.getTime();

        order.userGuid=userGuid;
        order.dateOrder=now.toString();
        order.pay=false;
        order.total=toPayProducto();
        order.productList=productList;
        usersRef.child(userGuid).setValue(order,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());

                } else {

                    System.out.println("xxxxxxx");
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
                            saveJSONFile(bytes);

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

        }
    }
    public void showError(String message){
        ViewDialog alert = new ViewDialog();
        alert.showDialog(MarketActivity.this, message);

    }
}
