package pt.iade.quickwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import pt.iade.quickwork.models.User;
import pt.iade.quickwork.models.Work;

public class Job_worker extends AppCompatActivity implements OnMapReadyCallback {
    TextView owner, users, workprice, worktype;
    int index = 0;
    MapView mapView;
    GoogleMap map;
    Button button;
    User user;
    ArrayList<User> arrayusers;
    Work work;
    JSONArray usersArray;
    JSONObject workObject;
    JSONobjdownloadtask task = new JSONobjdownloadtask();
    JSONarraydownloadtask task1 = new JSONarraydownloadtask();
    Button back_button, forward_button;



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

        user = (User) getIntent().getSerializableExtra("User");
        try {
            workObject =  task.execute(Constants.api_server +"work/user/"+user.getId()).get();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        //map.moveCamera(CameraUpdateFactory.);
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



}
