package pt.iade.quickwork.Job;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pt.iade.quickwork.Constants;
import pt.iade.quickwork.DownloadTasks.DeleteMappingwithoutwrite;
import pt.iade.quickwork.DownloadTasks.JSONarraydownloadtask;
import pt.iade.quickwork.DownloadTasks.JSONobjdownloadtask;
import pt.iade.quickwork.DownloadTasks.PatchwithoutWrite;
import pt.iade.quickwork.DownloadTasks.StringDownload;
import pt.iade.quickwork.R;
import pt.iade.quickwork.jobsmap;
import pt.iade.quickwork.models.User;
import pt.iade.quickwork.models.Work;
import pt.iade.quickwork.utilities;

public class Job_creator extends AppCompatActivity implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap map;
    Button button, previous, next;
    Work work;
    String Status;
    User loggedUser;
    TextView status_, worker;
    Boolean refresh = true, refresh_loc = false;
    JSONObject workobj = new JSONObject();
    ArrayList<User> urs = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    Marker usermarker;
    int workId;
    int index =0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_creator);
        mapView = (MapView) findViewById(R.id.mapViewCreator);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        loggedUser = (User) getIntent().getSerializableExtra("User");
        worker = (TextView) findViewById(R.id.textView_workers1);
        button = (Button) findViewById(R.id.button_cancelJob);
        next = (Button) findViewById(R.id.button_next1);
        previous = (Button) findViewById(R.id.button_back1);

        StringDownload task = new StringDownload();
        JSONobjdownloadtask task1 = new JSONobjdownloadtask();

        worker.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        previous.setVisibility(View.GONE);
        try {
            workobj = task1.execute(Constants.api_server + "work/owner/"+ loggedUser.getId()).get();
            task1.cancel(true);
            work = utilities.populate_work(workobj);
            workId = work.getId();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        status_ = findViewById(R.id.textView_showstatus);
        GetStatus();
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomGesturesEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setRotateGesturesEnabled(false);

        LatLng jobloc = new LatLng(work.getLat(), work.getLon());
        map.addMarker( new MarkerOptions().position(jobloc));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(jobloc, 15));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{

        }
        map.setMyLocationEnabled(true);
        //map.moveCamera(CameraUpdateFactory.);
    }

    public void refresh_status(int miliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                GetStatus();
            }
        };
        handler.postDelayed(runnable, miliseconds);
    }
    public void GetStatus() {
        String StatusTemp = null;
        StringDownload tasktemp1 = new StringDownload();
        try {
            StatusTemp = tasktemp1.execute(Constants.api_server + "work/getState/" +workId).get();
            tasktemp1.cancel(true);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if(StatusTemp == null){
            if(refresh){
                refresh_status(1000);
                Log.i("status", "couldnt get status");
            }
            return;
        }
        if(!StatusTemp.equals(Status)){
            Status = StatusTemp;
            status_.setText(StatusTemp);
        }
        if (Status.equals("Em espera")){
            JOBavailable();
        }else if(Status.equals("Em andamento")){
            JOBACCEPTEPT();
            Log.i("state", "accepted");
        }
        tasktemp1 = null;
        Log.i("refresh", "Still refreshing");
        if (refresh){refresh_status(1000);}
    }







    public void refresh_location(int miliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                GetLocation();
            }
        };
        handler.postDelayed(runnable, miliseconds);
    }
    public void GetLocation() {
        JSONobjdownloadtask taskloc = new JSONobjdownloadtask();
        JSONObject loc = new JSONObject();
        try {
            loc = taskloc.execute(Constants.api_server +"users/"+urs.get(index).getId()+"/location").get();
            taskloc.cancel(true);
            Log.i("work.loc",loc.toString());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            LatLng worker= new LatLng(loc.getDouble("lat"), loc.getDouble("lon"));
            map.clear();
            usermarker = map.addMarker(new MarkerOptions()
                    .position(worker)
                    .title(urs.get(index).getName())
            );
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(worker, 14));

        }catch (Exception e){e.printStackTrace();}
    taskloc = null;

        if (refresh_loc){refresh_location(1000);}
    }
    public void JOBavailable(){
        button.setText("Terminar trabalho");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refresh = false;
                CANCELJOB();
            }
        });
    }



    public void JOBACCEPTEPT(){
        refresh = false;
        refresh_loc = true;
        JSONArray usersArray = new JSONArray();
        JSONarraydownloadtask task1 = new JSONarraydownloadtask();
        try {
            usersArray = task1.execute(Constants.api_server+"work/users/"+work.getId()).get();
            Log.i("usersarrayowner", usersArray.toString());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        urs = populate_array(usersArray);




        GetLocation();
        worker.setVisibility(View.VISIBLE);
        button.setText("Acabar trabalho");
        worker.setText(urs.get(0).getName());
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refresh_loc = false;
                FINISHJOB(work);
            }
        });
        int size =urs.size();
        if(size >1 ) {
            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
            previous.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.i("button", "back pressed");
                    changetextView(0,urs);
                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    Log.i("button", "forward pressed");
                    changetextView(1,urs);
                }
            });
        }
    }


    public void changetextView(int direction, ArrayList<User> arrayusers){
        int size = arrayusers.size();

        //if recieves 0 go back if recieves 1 go forward
        if(size > 1) {
            if (direction == 1) {
                if (index != size-1){
                    index = index +1;
                    worker.setText(arrayusers.get(index).getName());
                    Log.i("button", arrayusers.get(index).getName());
                }else{
                    index = 0;
                    worker.setText(arrayusers.get(index).getName());
                    Log.i("button", arrayusers.get(index).getName());
                }
            } else {
                if (index != 0){
                    index = index -1;
                    worker.setText(arrayusers.get(index).getName());
                    Log.i("button", arrayusers.get(index).getName());
                }else{
                    index = size-1;
                    worker.setText(arrayusers.get(index).getName());
                    Log.i("button", arrayusers.get(index).getName());
                }
            }
        }
    }



    public void FINISHJOB(Work work){
        refresh_loc = false;
        PatchwithoutWrite finish = new PatchwithoutWrite();
        PatchwithoutWrite addJob = new PatchwithoutWrite();
        int response = 0;

        try {
            response = finish.execute(Constants.api_server+"work/finish/"+workId).get();
            for(int i= 0;i<urs.size(); i++){
                addJob.execute(Constants.api_server+"user/"+ids.get(i)+"/addjob");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if ( response == 200){
            Intent switchActivityIntent = new Intent(this, job_rating.class);
            switchActivityIntent.putExtra("User", loggedUser);
            startActivity(switchActivityIntent);
        }else {
            Toast.makeText(this, "ocorreu um erro a terminar", Toast.LENGTH_SHORT).show();
        }


    }
    public void CANCELJOB(){
        int response = 2;
        GetStatus();
        Log.i("status", Status);

        DeleteMappingwithoutwrite task = new DeleteMappingwithoutwrite();
        if(Status.equals("Em andamento")){
            Toast.makeText(this, "não dá para cancelar, trabalho já aceite", Toast.LENGTH_SHORT).show();
            status_.setText(Status);
            return;
        }

        try {
            response = task.execute(Constants.api_server+"work/cancel/"+work.getId()).get();
            if ( response == 200){
                Intent switchActivityIntent = new Intent(this, jobsmap.class);
                switchActivityIntent.putExtra("User", loggedUser);
                startActivity(switchActivityIntent);
            }else {
                Toast.makeText(this, "ocorreu um erro a cancelar", Toast.LENGTH_SHORT).show();
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> populate_array(JSONArray jsonArray){
        ArrayList<User> myUsers = new ArrayList<>();
        if(jsonArray != null) {
            User user= null;
            for (int i = 0; i < jsonArray.length();i++){
                try {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    user = utilities.populate_usr(jsonPart);
                    ids.add(i, jsonPart.getInt("id"));
                    myUsers.add(user);
                    user = null;
                }catch (Exception e){e.printStackTrace();}

            }

        }
        return myUsers;
    }



}