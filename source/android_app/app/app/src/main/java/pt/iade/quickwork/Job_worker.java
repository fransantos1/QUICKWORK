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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import pt.iade.quickwork.DownloadTasks.JSONobjdownloadtask;
import pt.iade.quickwork.DownloadTasks.Patchtask;
import pt.iade.quickwork.DownloadTasks.PatchwithoutWrite;
import pt.iade.quickwork.models.User;
import pt.iade.quickwork.models.Work;

public class Job_worker extends AppCompatActivity implements OnMapReadyCallback {
    TextView owner, users, workprice, worktype;
    int index = 0;
    MapView mapView;
    GoogleMap map;
    Button button;
    User userLogged;
    ArrayList<User> arrayusers;
    Work work;
    JSONArray usersArray;
    JSONObject workObject;
    JSONobjdownloadtask task = new JSONobjdownloadtask();
    JSONarraydownloadtask task1 = new JSONarraydownloadtask();
    Button back_button, forward_button;
    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_worker);


        mapView = (MapView) findViewById(R.id.mapView);
        button = (Button) findViewById(R.id.button_directions);
        back_button =(Button) findViewById(R.id.button_previous);
        forward_button =(Button) findViewById(R.id.button_next);
        owner = findViewById(R.id.textView_owner);
        users = findViewById(R.id.textView_workers);
        workprice = findViewById(R.id.textView_pricehr);
        worktype = findViewById(R.id.textView_type);

        userLogged = (User) getIntent().getSerializableExtra("User");
        try {
            workObject =  task.execute(Constants.api_server +"work/user/"+userLogged.getId()).get();
            int id = workObject.getInt("id");
            usersArray = task1.execute(Constants.api_server+"work/users/"+id).get();
        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        task1.cancel(true);
        task.cancel(true);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        arrayusers = populate_array(usersArray);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                directions();
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                changetextView(0);
            }
        });
        forward_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                changetextView(1);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            work = utilities.populate_work(workObject);
            Log.i("trabalho teste", work.getStarted_time()+"");
        }


        worktype.setText(work.getType());
        workprice.setText(work.getPricehr().toString());

        owner.setText(arrayusers.get(index).getName());
        index = index+1;
        users.setText(arrayusers.get(index).getName());



    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Double lat = null;
        Double lon = null;
        map = googleMap;

        map.getUiSettings().setZoomGesturesEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        try {
            lon = workObject.getDouble("lon");
            lat = workObject.getDouble("lat");
            Log.i("loc", ""+lat+lon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng jobloc = new LatLng(lat, lon);
        map.addMarker( new MarkerOptions().position(jobloc));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(jobloc, 15));
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Patchtask finish = new Patchtask();
                JSONObject loc = new JSONObject();
                try {
                    loc.put("lat", location.getLatitude());
                    loc.put("lon", location.getLongitude());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    finish.execute(Constants.api_server+"users/"+userLogged.getId()+"/location",loc.toString()).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        else
        {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    locationListener
            );
        }
        map.setMyLocationEnabled(true);
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



    public void changetextView(int direction){
        int size = arrayusers.size();
        Log.i("s", ""+size);
        //if recieves 0 go back if recieves 1 go forward
        if(size != 2) {
            if (direction == 1) {
                if (index != size-1){
                    index = index +1;
                    users.setText(arrayusers.get(index).getName());
                }else{
                    index = 1;
                    users.setText(arrayusers.get(index).getName());
                }
            } else {
                if (index != 1){
                    index = index -1;
                    users.setText(arrayusers.get(index).getName());
                }else{
                    index = size-1;
                    users.setText(arrayusers.get(index).getName());
                }
            }
        }
    }




    public ArrayList<User> populate_array(JSONArray jsonArray){

        ArrayList<User> myUsers = new ArrayList<>();
        if(jsonArray != null) {
            JSONObject owner = null;
            User user= null;

            //gets the owner of the job
            try {
                JSONobjdownloadtask task4 = new JSONobjdownloadtask();
                owner =  task4.execute(Constants.api_server +"users/owner/"+workObject.getInt("id")).get();
                user = utilities.populate_usr(owner);
                myUsers.add(user);
                user = null;
            } catch (ExecutionException | InterruptedException | JSONException e) {
                e.printStackTrace();
            }
            //gets the rest of the users
            for (int i = 0; i < jsonArray.length();i++){
                try {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    user = utilities.populate_usr(jsonPart);
                    myUsers.add(user);
                    user = null;
                }catch (Exception e){e.printStackTrace();}

            }

        }
        return myUsers;
    }





    //button to give directions
    public void directions(){
        Double lat = null;
        Double lon = null;
        Log.i("loc", workObject.toString());
        try {
            lon = workObject.getDouble("lon");
            lat = workObject.getDouble("lat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Uri gmmIntentUri = Uri.parse("google.navigation:q=("+lat+lon+")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        Log.i("button", "directions selected");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0,
                        locationListener
                );
            }
        }
    }


}
