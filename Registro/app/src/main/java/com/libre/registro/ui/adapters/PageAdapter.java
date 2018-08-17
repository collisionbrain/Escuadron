package com.libre.registro.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.libre.registro.ui.fragments.DigitalCodeRegister;
import com.libre.registro.ui.fragments.HealtDataRegister;
import com.libre.registro.ui.fragments.PersonalDataRegister;
import com.libre.registro.ui.fragments.SignatureRegister;

public class PageAdapter extends FragmentPagerAdapter {
    private Context contexto;

    public PageAdapter(Context context, FragmentManager fm) {
        super(fm);
        contexto=context;

    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragmento = new Fragment();
        switch(position){
            case 0:
                fragmento =new PersonalDataRegister();
                break;
            case 1:
                fragmento =new HealtDataRegister();
                break;

            case 2:
                fragmento =new SignatureRegister();
                break;

            case 3:
                fragmento =new DigitalCodeRegister();
                break;

        }
        return fragmento;
    }
    @Override
    public int getCount() {
        return 4;
    }

}