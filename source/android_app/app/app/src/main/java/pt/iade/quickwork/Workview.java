package pt.iade.quickwork;

import pt.iade.quickwork.downloadTask.JSONarraydownloadtask;
import pt.iade.quickwork.downloadTask.JSONobjdownloadtask;
import pt.iade.quickwork.downloadTask.TypeDownloadtask;
import pt.iade.quickwork.downloadTask.downloadowners;
import pt.iade.quickwork.models.User;
import pt.iade.quickwork.models.work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;


public class Workview extends AppCompatActivity {

    JSONArray getowner = null;
    JSONArray selWork = null;
    JSONArray worktype = null;
    private String tempid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workview);

        downloadowners task3 = new downloadowners();
        JSONobjdownloadtask task = new JSONobjdownloadtask();
        TypeDownloadtask task1 = new TypeDownloadtask();
        int id = Integer.parseInt(getIntent().getStringExtra("workid"));


        // get work information
        try {
            selWork = task.execute(Constants.api_server +"work/"+ id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            selWork = null;
        }
        //get worktype information
        try {
            String worktypeid = selWork.getString(5);
            try {
                worktype = task1.execute(Constants.api_server +"work/type/"+ worktypeid).get();
            }catch (ExecutionException e) {
                e.printStackTrace();
                selWork = null;
            }

        }catch ( Exception e){
            e.printStackTrace();
        }

        //get owner
        try {
            String workid = selWork.getString(0);
            try {
                getowner = task3.execute(Constants.api_server +"users/owner/"+ workid).get();
            }catch (ExecutionException e) {
                e.printStackTrace();
                selWork = null;
            }

        }catch ( Exception e){
            e.printStackTrace();
        }


        //change to profile
        Log.i("Worktypetest", worktype.toString());
        Log.i("Worktteste", selWork.toString());
        populatework();

        Button button = (Button) findViewById(R.id.buttonentrar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchtoprofileview();
            }
        });



    }


    private void switchtoprofileview() {
        User user = getowner();
        Intent switchActivityIntent = new Intent(this, ProfileActivity1.class);
        Log.i("teste", user.getName());
        switchActivityIntent.putExtra("User", user);
        startActivity(switchActivityIntent);
    }


    private void populatework(){
        work work = getWork();
        User user = getowner();

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
        if(getowner != null){
            try{
                int id = getowner.getInt(0);
                String nome= getowner.getString(1);
                String email = getowner.getString(2);
                int jobn = getowner.getInt(3);
                int rating = getowner.getInt(4);
                user = new User(id, nome, email, jobn, rating);

            }catch (Exception e) {
                e.printStackTrace();
            }


        }



        return user;
    }



    private work getWork() {

        work work = null;
        if (selWork != null) {
            try {
                Log.i ("s", selWork.getString(1));
                Log.i ("s", selWork.getString(6));
                Log.i ("s", selWork.getString(7));

                int id = selWork.getInt(0);
                Double pricehr = selWork.getDouble(1);
                Double tip = null;
                LocalDate started_time = null;
                LocalDate finished_time = null;
                String type = worktype.getString(0);
                double lat = selWork.getDouble(6);
                double lon = selWork.getDouble(7);
                work = new work(id, pricehr, null, null, null, type, lat, lon);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return work;


    }








}