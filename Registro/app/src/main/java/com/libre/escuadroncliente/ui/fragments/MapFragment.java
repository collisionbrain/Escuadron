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
    private SubmitButton btnGuardar;
    private  Marker startMarker;
    private GeoPoint pointCenter;

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.map, null);
        mMapView = v.findViewById(R.id.mapview1);
        btnGuardar=v.findViewById(R.id.btnFinishMap);
        context=getActivity();
        startMarker = new Marker(mMapView);
        location = AppLocation.getLocation(context);
        if(location!=null){
            longitude =  location.getLongitude();
            latitude =    location.getLatitude();
        }else{
            String bestProvider=AppLocation.getbestProvider();
            AppLocation.getLocationManager().requestLocationUpdates(bestProvider, 1000, 0, this);
        }
        mMapView.setBuiltInZoomControls(false);
        mMapView.setMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {

                loadMarker();
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {

                return false;
            }
        }, 200));

        mMapView.setMultiTouchControls(true);
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
        setLocationInMap(location.getLatitude(),location.getLongitude());

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
        mMapView.getOverlays().add(startMarker);
        startMarker.setIcon(getResources().getDrawable(R.drawable.alocation));
        startMarker.setTitle("Aqui nos vemos");
        startMarker.showInfoWindow();
    }

    private void loadMarker(){
        pointCenter=new GeoPoint(
                mMapView.getMapCenter().getLatitude(),
                mMapView.getMapCenter().getLongitude());

        startMarker.setPosition(pointCenter);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlays().add(startMarker);
        startMarker.setIcon(getResources().getDrawable(R.drawable.alocation));
        startMarker.setTitle("Start point");
        startMarker.showInfoWindow();
    }

    SubmitButton.OnResultEndListener finishListenerMap=new SubmitButton.OnResultEndListener() {
        @Override
        public void onResultEnd() {
        ((MarketActivity) context).closeMapFragment(
                    startMarker.getPosition().getLatitude(),
                    startMarker.getPosition().getLongitude());

        }
    };


}
