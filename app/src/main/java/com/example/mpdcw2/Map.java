// Name                 Scott Adams
// Student ID           S2137174

package com.example.mpdcw2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap mMap) {
                googleMap = mMap;

                //drop a marker
                LatLng glasgow = new LatLng(55.86695, -4.24997);
                LatLng london = new LatLng(51.51907, -0.07185);
                LatLng newYork = new LatLng(40.72349, -74.00168);
                LatLng oman = new LatLng(23.64086, 58.21922);
                LatLng bang =  new LatLng(23.87208, 90.37718);
                LatLng mauritius = new LatLng(-20.10177, 57.56302);
                googleMap.addMarker(new MarkerOptions().position(glasgow).title("GCU").snippet("GCU Glasgow Campus"));
                googleMap.addMarker(new MarkerOptions().position(london).title("GCU London").snippet("Shoreditch University"));
                googleMap.addMarker(new MarkerOptions().position(newYork).title("GCU NYC").snippet("New York Campus"));
                googleMap.addMarker(new MarkerOptions().position(oman).title("GCU Oman").snippet("National University of Science and Tech"));
                googleMap.addMarker(new MarkerOptions().position(bang).title("GCU Bangladesh").snippet("Grameen Caledonian College of Nursing"));
                googleMap.addMarker(new MarkerOptions().position(mauritius).title("GCU Mauritius").snippet("African Leadership College"));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(glasgow).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}