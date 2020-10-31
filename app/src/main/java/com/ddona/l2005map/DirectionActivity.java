package com.ddona.l2005map;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback {
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
    }
}