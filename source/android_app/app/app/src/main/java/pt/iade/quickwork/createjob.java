package pt.iade.quickwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pt.iade.quickwork.DownloadTasks.JSONarraydownloadtask;
import pt.iade.quickwork.models.User;
import pt.iade.quickwork.DownloadTasks.PutTask;
import pt.iade.quickwork.models.Work;

public class createjob extends AppCompatActivity implements OnMapReadyCallback {
    User loggedUser;
    Spinner spinnertype;
    Button button;
    TextView editpricehr;
    MapView mapView;
    GoogleMap map;
    LatLng JobLoc;

    LocationManager locationManager;
    LocationListener locationListener;


    ArrayList<String> type = new ArrayList<>();
    JSONArray types = new JSONArray();
    JSONarraydownloadtask task = new JSONarraydownloadtask();

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
        setContentView(R.layout.activity_createjob);

        mapView = (MapView) findViewById(R.id.mapView_Jobcreation);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        loggedUser = (User) getIntent().getSerializableExtra("User");
        //POPULATE DROPLIST
        try {
            types = task.execute(Constants.api_server+"worktype").get();
            task.cancel(true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        type = dropdown_type(types);
        spinnertype=findViewById(R.id.spinner_type);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertype.setAdapter(dataAdapter);

        //BUTTON LISTENER
        editpricehr = findViewById(R.id.editText_pricehr);


        button = (Button) findViewById(R.id.button_createjob);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addwork();
            }
        });
    }


    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null){
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            }
            map.setMyLocationEnabled(true);
        }


        map.getUiSettings().setMyLocationButtonEnabled(false);
        //map.moveCamera(CameraUpdateFactory.zoomTo(15));

        //map.addMarker( new MarkerOptions().position(jobloc));


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                map.clear();
                JobLoc = new LatLng(point.latitude, point.longitude);
                map.addMarker(new MarkerOptions().position(point));

            }
        });

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }









    private ArrayList<String> dropdown_type(JSONArray types){
        ArrayList<String> type = new ArrayList<>();

        if(types != null){
            for (int i = 0; i < types.length(); i++){
                try {
                    JSONObject jsonpart = types.getJSONObject(i);
                    type.add(jsonpart.getString("name")+"    "+jsonpart.getDouble("pricehr")+"€/hr");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return type;
    }

    private void addwork(){

        PutTask createJob = new PutTask();
        JSONObject workobject = new JSONObject();
        Integer response= 0;
        Work work = createWork();
        if(work != null){
            workobject = utilities.Work_JSON(work);
            Log.i( "teste", workobject.toString());
            Log.i( "teste", work.getType());
        }else{
            Log.i("work", "uploadwork is null");
        }
        try {
           response = createJob.execute(Constants.api_server+"work/"+loggedUser.getId(), workobject.toString()).get();
           createJob = null;

            Intent switchActivityIntent = new Intent(this, Job_creator.class);
            switchActivityIntent.putExtra("User", loggedUser);
            startActivity(switchActivityIntent);
            Log.i("user", "is occuppied");










        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if(response == 200){
            Log.i("resposta", ""+response);
        }else{
            Toast.makeText(this, "aconteceu algum erro em criar o trabalho", Toast.LENGTH_SHORT).show();
        }


    }

    private Work createWork(){
        Work work;
        String type= null;
        Double pricehr= null;
        LatLng loc = null;
        String parts[];

        parts = spinnertype.getSelectedItem().toString().split(" ");
        type = parts[0];
        if (editpricehr.getText().toString().matches("")) {
            Toast.makeText(this, "por favor insira preço do trabalho", Toast.LENGTH_SHORT).show();
            return null;
        }
        pricehr = Double.valueOf(editpricehr.getText().toString());
        Log.i("pricehr", ""+pricehr);
        if(JobLoc == null){
            Toast.makeText(this, "por favor selecione no mapa a localização", Toast.LENGTH_SHORT).show();
            return null;
        }
        loc = JobLoc;
        work = new Work(pricehr, type, loc.latitude, loc.longitude);
        return work;







    }






}