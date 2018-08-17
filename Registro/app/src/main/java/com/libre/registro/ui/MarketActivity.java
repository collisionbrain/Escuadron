package com.libre.registro.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.libre.registro.R;
import com.libre.registro.ui.adapters.PageAdapter;
import com.libre.registro.ui.util.NonSwipeableViewPager;

public class MarketActivity extends FragmentActivity {

    private NonSwipeableViewPager vwPaginas;
    private PageAdapter adPaginador;
    private Context context;
    private TextView txtTitulo;
    public Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_activity);
        context = this;
        resources=getResources();
        vwPaginas=(NonSwipeableViewPager) findViewById(R.id.id_viewpager);
        txtTitulo=(TextView) findViewById(R.id.txtTitulo);
        adPaginador=new PageAdapter(getApplicationContext(),getSupportFragmentManager());
        vwPaginas.setAdapter(adPaginador);
        vwPaginas.setCurrentItem(0);

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

    @Override
    public void onResume() {
        super.onResume();

    }


}
