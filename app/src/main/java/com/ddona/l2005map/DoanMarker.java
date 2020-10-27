package com.ddona.l2005map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class DoanMarker implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public DoanMarker(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_marker, null);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
