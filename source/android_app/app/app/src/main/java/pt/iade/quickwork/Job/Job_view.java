package pt.iade.quickwork.Job;

import pt.iade.quickwork.Constants;
import pt.iade.quickwork.DownloadTasks.JSONobjdownloadtask;
import pt.iade.quickwork.DownloadTasks.PatchwithoutWrite;
import pt.iade.quickwork.DownloadTasks.TypeDownloadtask;
import pt.iade.quickwork.Job.Job_worker;
import pt.iade.quickwork.ProfileActivity1;
import pt.iade.quickwork.R;
import pt.iade.quickwork.models.User;
import pt.iade.quickwork.models.Work;
import pt.iade.quickwork.utilities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class Job_view extends AppCompatActivity {

    JSONObject getownerobj = null;
    JSONObject selWork = null;
    private String tempid;
    User owner;
    User LoggedUser;
    Button button, accept_button;
    Work work;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workview);
        LoggedUser = (User) getIntent().getSerializableExtra("User");
        JSONobjdownloadtask ownertask = new JSONobjdownloadtask();
        JSONobjdownloadtask task = new JSONobjdownloadtask();
        TypeDownloadtask task1 = new TypeDownloadtask();

        int id = Integer.parseInt(getIntent().getStringExtra("workid"));
        Log.i("teste", "got here");

        // get work information
        try {
            selWork = task.execute(Constants.api_server +"work/"+ id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            selWork = null;
        }

        work = getWork();
        //get owner
        try {
            try {
                getownerobj = ownertask.execute(Constants.api_server +"users/owner/"+ work.getId()).get();
                Log.i("user", getownerobj.toString());
            }catch (ExecutionException e) {
                e.printStackTrace();
                selWork = null;
            }

        }catch ( Exception e){
            e.printStackTrace();
        }
        owner = getowner();

        Log.i("owner", owner.getEmail());

        Log.i("Worktteste", selWork.toString());
        populatework();
        button = (Button) findViewById(R.id.buttonentrar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchtoprofileview();
            }
        });
        accept_button = (Button) findViewById(R.id.button_acceptwork);
        accept_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                acceptJOB();
            }
        });


    }
    private void acceptJOB(){
        int response;
        PatchwithoutWrite task1 = new PatchwithoutWrite();
        try {
            response = task1.execute(Constants.api_server+"work/accept/"+LoggedUser.getId()+"/"+work.getId()).get();
            if ( response == 200){
                Intent switchActivityIntent = new Intent(this, Job_worker.class);
                switchActivityIntent.putExtra("User", LoggedUser);
                startActivity(switchActivityIntent);
            }else {
                Toast.makeText(this, "ocorreu um erro a aceitar", Toast.LENGTH_SHORT).show();
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //change to profile
    private void switchtoprofileview() {
        Intent switchActivityIntent = new Intent(this, ProfileActivity1.class);
        Log.i("teste", owner.getName());
        switchActivityIntent.putExtra("User", owner);
        startActivity(switchActivityIntent);
    }


    private void populatework(){
        User user = owner;

        TextView type = (TextView) (findViewById(R.id.textviewtype));
        TextView pricehr = (TextView) (findViewById(R.id.textviewpreco));
        TextView loc = (TextView) (findViewById(R.id.textviewloc));
        TextView owner = (TextView)(findViewById(R.id.textviewOwner));
        String name = user.getName();
        String typestr = work.getType();
        String pricehrstr = work.getPricehr()+ "";
        String locstr = work.getLat()+" "+ work.getLon()+"";
        type.setText(typestr);
        pricehr.setText(pricehrstr);
        owner.setText(name);
        loc.setText(locstr);
        Log.i("WORKInFo", typestr + pricehrstr + locstr);
    }

    private User getowner(){
        User user = null;
        if(getownerobj != null){
            try{
                /*
                int id = getownerobj.getInt("id");
                String nome= getownerobj.getString("name");
                String email = getownerobj.getString("email");
                int jobn = getownerobj.getInt("jobnumber");
                Integer rating = getownerobj.getInt("rating");
                user = new User(id, nome, email, jobn, rating);

                   */
                user = utilities.populate_usr(getownerobj);
                //Log.i("user", owner.getName());
            }catch (Exception e) {
                e.printStackTrace();
            }


        }
        return user;
    }

    private Work getWork() {

        Work work = null;
        if (selWork != null) {
            try {
                /*
                int id = selWork.getInt("id");
                Double pricehr = selWork.getDouble("pricehr");
                String type = selWork.getString("type");
                double lat = selWork.getDouble("lat");
                double lon = selWork.getDouble("lon");
                work = new Work(id, pricehr, null, null, null, type, lat, lon);*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    work = utilities.populate_work(selWork);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return work;


    }








}