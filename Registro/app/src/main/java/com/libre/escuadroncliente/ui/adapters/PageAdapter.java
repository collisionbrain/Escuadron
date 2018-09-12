package com.libre.escuadroncliente.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.libre.escuadroncliente.ui.fragments.AddressDataRegister;
import com.libre.escuadroncliente.ui.fragments.CredentialFragment;
import com.libre.escuadroncliente.ui.fragments.CronicSuffering;
import com.libre.escuadroncliente.ui.fragments.DigitalCodeRegister;
import com.libre.escuadroncliente.ui.fragments.HealtDataRegister;
import com.libre.escuadroncliente.ui.fragments.PersonalDataRegister;
import com.libre.escuadroncliente.ui.fragments.SignatureRegister;

public class PageAdapter extends FragmentPagerAdapter {
    private Context contexto;
    Fragment fragmento;
    Fragment fragmentoCredencial;

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
                fragmento =new HealtDataRegister();
                break;
            case 3:
                fragmento =new CronicSuffering();
                break;
            case 4:
                fragmentoCredencial= new CredentialFragment();
                fragmento =fragmentoCredencial;
                break;
            case 5:
                fragmento =new SignatureRegister();
                break;
            case 6:
                fragmento =new DigitalCodeRegister();
                break;
        }
        return fragmento;
    }
    @Override
    public int getCount() {
        return 6;
    }


    public Fragment getItemCode() {

        return fragmento;
    }
    public Fragment getItemCurrentFragment() {

        return fragmento;
    }
    public Fragment getCredentialFragment(){
        return fragmentoCredencial;
    }
}