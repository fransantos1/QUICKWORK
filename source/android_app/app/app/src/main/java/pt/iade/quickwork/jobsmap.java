package pt.iade.quickwork;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pt.iade.quickwork.databinding.ActivityJobsmapBinding;
import pt.iade.quickwork.JSONtasks.JSONarraydownloadtask;
import pt.iade.quickwork.models.User;

public class jobsmap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityJobsmapBinding binding;
    private GoogleMap.OnMarkerClickListener markerlistener;
    LocationManager locationManager;
    LocationListener locationListener;
    JSONArray arrayWorks = null;
    User LoggedUser;
    Button create_job;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobsmapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LoggedUser = (User) getIntent().getSerializableExtra("User");

        JSONarraydownloadtask task = new JSONarraydownloadtask();
        try {
            Log.i("JOBS", "started ");
            arrayWorks = task.execute(Constants.api_server + "work").get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            arrayWorks = null;
        }
        create_job = (Button) findViewById(R.id.button_addwork);
        create_job.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch_CreateJob();
            }
        });


    }
    private void switch_CreateJob(){
        Intent switchActivityIntent = new Intent(this, createjob.class);
        switchActivityIntent.putExtra("User", LoggedUser);
        startActivity(switchActivityIntent);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mMap = googleMap;
        Marker workmarker;
        Location usrLoc = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        if(arrayWorks != null) {
            ArrayList<String> myItems = new ArrayList<>();
            for (int i = 0; i < arrayWorks.length();i++){
                try {
                    JSONObject jsonPart = arrayWorks.getJSONObject(i);
                    LatLng work = new LatLng(jsonPart.getDouble("lat"), jsonPart.getDouble("lon"));
                    workmarker = mMap.addMarker(new MarkerOptions()
                            .position(work)
                            .title(jsonPart.getString("type"))
                    );
                    workmarker.setTag(jsonPart.getString("id"));
                }catch (Exception e){e.printStackTrace();}
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    public void onInfoWindowClick(Marker marker) {
                        String workid = (String) marker.getTag();

                        switchtoworkview(workid);

                    }
                });
            }
        }


    }
    private void switchtoworkview(String id) {
        Log.i( "id", id);
        Intent switchActivityIntent = new Intent(this, Workview.class);
        switchActivityIntent.putExtra("workid", id);
        startActivity(switchActivityIntent);
    }





        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
}