package com.libre.escuadroncliente.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.libre.escuadroncliente.R;
import com.libre.escuadroncliente.ui.MarketActivity;
import com.libre.escuadroncliente.ui.util.AppLocation;
import com.unstoppable.submitbuttonview.SubmitButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapFragment extends Fragment implements LocationListener {
    private MapView mMapView;
    private double longitude;
    private double latitude;
    private Context context;
    private  IMapController mapController;
    private LocationManager locationManager;
    private Location location;
    private SubmitButton btnElegir,btnGuardar;
    private  Marker startMarker;
    private GeoPoint pointCenter;
    private boolean previus=false;
    private boolean devlivery=false;
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.map, null);
        mMapView = v.findViewById(R.id.mapview1);
        btnGuardar=v.findViewById(R.id.btnFinishMap);
        btnElegir=v.findViewById(R.id.btnChoiceMap);
        context=getActivity();
        startMarker = new Marker(mMapView);
        devlivery=((MarketActivity) context).isDeliveryActive;


            if (((MarketActivity) context).order.latitude != 0.0 &&
                    ((MarketActivity) context).order.longitude != 0.0) {
                longitude = ((MarketActivity) context).order.longitude;
                latitude = ((MarketActivity) context).order.latitude;
                previus = true;
            } else {
                location = AppLocation.getLocation(context);
                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                } else {
                    String bestProvider = AppLocation.getbestProvider();
                    AppLocation.getLocationManager().requestLocationUpdates(bestProvider, 1000, 0, this);
                }
            }


       if(!devlivery){
           mMapView.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {


                   return true;
               }
           });
       }

        mMapView.setBuiltInZoomControls(false);
        mMapView.setMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                btnGuardar.reset();
                btnGuardar.setVisibility(View.GONE);
                btnElegir.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {

                return false;
            }
        }, 200));

        mMapView.setMultiTouchControls(true);
        btnElegir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnElegir.setOnResultEndListener(choiceListenerMap);
                btnElegir.doResult(true);
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGuardar.setOnResultEndListener(finishListenerMap);
                btnGuardar.doResult(true);
            }
        });
        return v;

    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);


    }

    @Override
    public void onPause(){
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        if(devlivery){

            if(!previus) {
                setLocationInMap(location.getLatitude(), location.getLongitude());
            }else{
                setLocationInMap(latitude, longitude);
            }

        }else {
            latitude = 19.427171;
            longitude=  -99.166741;
            setLocationInMap(latitude, longitude);
        }


        if (mMapView != null) {

            mMapView.onResume();
        }

       getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    return true;

                }
                return false;
            }
        } );
    }
    @Override
    public void onLocationChanged(Location location) {
        if( !this.location.equals(location)){
            this.location=location;
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public void setLocationInMap(double latitude,double longitude){
        GeoPoint point = new GeoPoint(latitude, longitude);
        mapController = mMapView.getController();
        mapController.setZoom(19);
        mapController.setCenter(point);
        mapController.animateTo(point);
        startMarker.setPosition(point);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        //mMapView.getOverlays().add(startMarker);
        startMarker.setIcon(getResources().getDrawable(R.drawable.alocation));
        startMarker.setTitle("Aqui nos vemos");
        startMarker.showInfoWindow();
    }

    private void loadMarker(){
        pointCenter=new GeoPoint(
                mMapView.getMapCenter().getLatitude(),
                mMapView.getMapCenter().getLongitude());

        startMarker.setPosition(pointCenter);
        startMarker.setIcon(getResources().getDrawable(R.drawable.alocation));
        startMarker.setTitle("Aqui nos vemos");
        startMarker.showInfoWindow();

        btnElegir.setVisibility(View.GONE);
        btnGuardar.setVisibility(View.VISIBLE);
        btnGuardar.setOnResultEndListener(finishListenerMap);
        btnGuardar.doResult(true);
    }

    SubmitButton.OnResultEndListener finishListenerMap=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {

            double latitude=startMarker.getPosition().getLatitude();
            double longitude=startMarker.getPosition().getLongitude();
            ((MarketActivity) context).order.latitude=latitude;
            ((MarketActivity) context).order.longitude=longitude;

            ((MarketActivity) context).closeMapFragment();

        }
    };
    SubmitButton.OnResultEndListener choiceListenerMap=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {

             loadMarker();
        }
    };

}
