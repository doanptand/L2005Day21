package com.ddona.l2005map;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private Button btnFind;
    private EditText edtStart, edtEnd;
    private TextView tvTime, tvDistance;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        initView();
    }

    private void initView() {
        btnFind = findViewById(R.id.btn_find);
        edtStart = findViewById(R.id.edt_start);
        edtEnd = findViewById(R.id.edt_end);
        tvDistance = findViewById(R.id.tv_distance);
        tvTime = findViewById(R.id.tv_time);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_direction);
        mapFragment.getMapAsync(this);
        btnFind.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_find) {
            findDirection();
        }
    }

    private void findDirection() {
        String origin = edtStart.getText().toString();
        String destination = edtEnd.getText().toString();
        if (origin.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "You are so stupid, enter what you want",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            DirectionsResult result = DirectionsApi.newRequest(getGeoContext())
                    .origin(origin)
                    .destination(destination)
                    .avoid(DirectionsApi.RouteRestriction.HIGHWAYS)
                    .mode(TravelMode.DRIVING)
                    .departureTime(new DateTime())
                    .alternatives(true)
                    .optimizeWaypoints(true).await();
            displayInfo(result);
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayInfo(DirectionsResult result) {
        mMap.clear();
        if (result.routes.length == 0) {
            Toast.makeText(this,"Can't find any way for your request!", Toast.LENGTH_SHORT).show();
            return;
        }

        MarkerOptions originOption = new MarkerOptions();
        LatLng originLatLng = new LatLng(
                result.routes[0].legs[0].startLocation.lat,
                result.routes[0].legs[0].startLocation.lng);
        originOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.marker_a));
        originOption.position(originLatLng);

        MarkerOptions destinationOption = new MarkerOptions();
        LatLng destinationLatLng = new LatLng(
                result.routes[0].legs[0].endLocation.lat,
                result.routes[0].legs[0].endLocation.lng);
        destinationOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.marker_b));
        destinationOption.position(destinationLatLng);

        mMap.addMarker(originOption);
        mMap.addMarker(destinationOption);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(originLatLng, 15));

        String duration = result.routes[0].legs[0].duration.humanReadable;
        tvTime.setText(duration);

        String distance = result.routes[0].legs[0].distance.humanReadable;
        tvTime.setText(distance);


        List<LatLng> paths = PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath());
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(paths);
        mMap.addPolyline(polylineOptions);


    }

    private GeoApiContext getGeoContext() {
        GeoApiContext context = new GeoApiContext();
        context.setApiKey(getString(R.string.google_maps_key));
        context.setConnectTimeout(20, TimeUnit.SECONDS);
        return context;
    }
}