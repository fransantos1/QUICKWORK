package pt.iade.quickwork;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import pt.iade.quickwork.DownloadTasks.JSONobjdownloadtask;
import pt.iade.quickwork.DownloadTasks.Patchtask;
import pt.iade.quickwork.DownloadTasks.StringDownload;
import pt.iade.quickwork.models.User;
import pt.iade.quickwork.models.Work;

public class Job_creator extends AppCompatActivity implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap map;
    Button button;
    Work work;
    String Status;
    User loggedUser;
    TextView status_;
    JSONObject workobj = new JSONObject();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_creator);
        loggedUser = (User) getIntent().getSerializableExtra("User");
        mapView = (MapView) findViewById(R.id.mapViewCreator);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        StringDownload task = new StringDownload();
        JSONobjdownloadtask task1 = new JSONobjdownloadtask();



        try {
            workobj = task1.execute(Constants.api_server + "work/owner/"+ loggedUser.getId()).get();
            work = utilities.populate_work(workobj);
            Log.i("work", workobj.toString());
            Status = task.execute(Constants.api_server + "work/getState/"+work.getId()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        status_ = findViewById(R.id.textView_showstatus);
        status_.setText(Status);
        task.cancel(true);
        button = (Button) findViewById(R.id.button_cancelJob);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CANCELJOB(work);
            }
        });

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

    public void CANCELJOB(Work work){
        int response;
        Patchtask task = new Patchtask();
        try {
            response = task.execute(Constants.api_server+"work/setState/4", workobj.toString()).get();
            if ( response == 200){
                Intent switchActivityIntent = new Intent(this, Job_worker.class);
                switchActivityIntent.putExtra("User", loggedUser);
                startActivity(switchActivityIntent);
            }else {
                Toast.makeText(this, "ocorreu um erro a cancelar", Toast.LENGTH_SHORT).show();
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}