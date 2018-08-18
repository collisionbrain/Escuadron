package com.libre.registro.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.libre.registro.R;
import com.libre.registro.ui.adapters.PageAdapter;
import com.libre.registro.ui.util.NonSwipeableViewPager;

public class MainActivity extends FragmentActivity {

    private NonSwipeableViewPager vwPaginas;
    private PageAdapter adPaginador;
    private Context context;
    private TextView txtTitulo;
    public Resources resources;
    public Dialog dialogError,dialogPrivacy;
    public   TextView messageError;
    private FirebaseStorage storage;
    final long ONE_MEGABYTE = 1024 * 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        context = this;
        resources=getResources();
        vwPaginas=(NonSwipeableViewPager) findViewById(R.id.id_viewpager);
        txtTitulo=(TextView) findViewById(R.id.txtTitulo);
        adPaginador=new PageAdapter(getApplicationContext(),getSupportFragmentManager());
        vwPaginas.setAdapter(adPaginador);
        vwPaginas.setCurrentItem(0);
        dialogError = new Dialog(context);
        dialogPrivacy= new Dialog(context);
        dialogPrivacy.setContentView(R.layout.dialog_privacy);
        dialogError.setContentView(R.layout.dialog_error);
        messageError=(TextView)dialogError .findViewById(R.id.txtMensaje);
        vwPaginas.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int position) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                switch(position){
                    case 0:
                        txtTitulo.setText(resources.getString(R.string.stPersonal));
                        break;
                    case 1:
                        txtTitulo.setText(resources.getString(R.string.stPersonal));
                        break;
                    case 2:
                        txtTitulo.setText(resources.getString(R.string.stPersonal));

                        break;
                    case 3:
                        txtTitulo.setText(resources.getString(R.string.strCode));
                        break;


                }
            }

        });
    }
    public void onActivityResult(int requestCode, int resultCode,   Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         }

    @Override
    public void onBackPressed(){

        int current=vwPaginas.getCurrentItem();
        if(current>0) {
            vwPaginas.setCurrentItem(current - 1);
        }else{
            super.onBackPressed();
        }
    }
    public void paginaSiguiente(int paginaSiguiente){
        vwPaginas.setCurrentItem(paginaSiguiente, true);

    }
    @Override
    public void onResume() {
        super.onResume();

    }


}
