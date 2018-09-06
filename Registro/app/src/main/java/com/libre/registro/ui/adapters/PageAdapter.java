package com.libre.registro.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.libre.registro.ui.fragments.AddressDataRegister;
import com.libre.registro.ui.fragments.CredentialFragment;
import com.libre.registro.ui.fragments.CronicSuffering;
import com.libre.registro.ui.fragments.DigitalCodeRegister;
import com.libre.registro.ui.fragments.HealtDataRegister;
import com.libre.registro.ui.fragments.PersonalDataRegister;
import com.libre.registro.ui.fragments.SignatureRegister;

public class PageAdapter extends FragmentPagerAdapter {
    private Context contexto;
    Fragment fragmento;

    public PageAdapter(Context context, FragmentManager fm) {
        super(fm);
        contexto=context;

    }
    @Override
    public Fragment getItem(int position) {
         fragmento = new Fragment();
        switch(position){
            case 0:
                fragmento =new PersonalDataRegister();
                break;
            case 1:
                fragmento =new AddressDataRegister();
                break;
            case 2:
                fragmento =new SignatureRegister();
                break;
            case 3:
                fragmento =new CredentialFragment();
                break;
            case 4:
                fragmento =new HealtDataRegister();
                break;
            case 5:
                fragmento =new CronicSuffering();
                break;
            case 6:
                fragmento =new DigitalCodeRegister();
                break;
        }
        return fragmento;
    }
    @Override
    public int getCount() {
        return 5;
    }


    public Fragment getItemCode() {

        return fragmento;
    }
    public Fragment getItemCurrentFragment() {

        return fragmento;
    }

}