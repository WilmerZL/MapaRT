package com.example.mapart;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap Mapa;
    Marker marker = null;


    DatabaseReference coordinatesRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference LatitudinRef, LongitudinRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

            // Conectamos el fragment a Google Maps
            SupportMapFragment mapFragment = (SupportMapFragment)
                    getSupportFragmentManager()
                            .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            LatitudinRef = database.getReference("coordenadas/latitud");
            LongitudinRef = database.getReference("coordenadas/longitud");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        coordinatesRef = mDatabase.child("coordenadas");

        coordinatesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                actualizarMarcadorMapa(snapshot.child("latitud").getValue(Double.class),
                        snapshot.child("longitud").getValue(Double.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Mapa = googleMap;
        Mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        Mapa.getUiSettings().setZoomControlsEnabled(true);

    }
    private void actualizarMarcadorMapa(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);

        if (marker == null)
            marker = Mapa.addMarker(new MarkerOptions().position(latLng).title("Tu Pos"));
        else
            marker.setPosition(latLng);

        Mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}