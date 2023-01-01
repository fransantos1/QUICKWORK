package pt.iade.quickwork;

import pt.iade.quickwork.JSONtasks.JSONobjdownloadtask;
import pt.iade.quickwork.JSONtasks.TypeDownloadtask;
import pt.iade.quickwork.JSONtasks.downloadowners;
import pt.iade.quickwork.models.User;
import pt.iade.quickwork.models.Work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class Workview extends AppCompatActivity {

    JSONArray getowner = null;
    JSONObject selWork = null;
    private String tempid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workview);

        downloadowners task3 = new downloadowners();
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
        //get owner
        try {
            String workid = selWork.getString("id");
            try {
                getowner = task3.execute(Constants.api_server +"users/owner/"+ workid).get();
                Log.i("user", getowner.toString());
            }catch (ExecutionException e) {
                e.printStackTrace();
                selWork = null;
            }

        }catch ( Exception e){
            e.printStackTrace();
        }



        Log.i("Worktteste", selWork.toString());
        populatework();

        Button button = (Button) findViewById(R.id.buttonentrar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchtoprofileview();
            }
        });



    }




    //change to profile
    private void switchtoprofileview() {
        User user = getowner();
        Intent switchActivityIntent = new Intent(this, ProfileActivity1.class);
        Log.i("teste", user.getName());
        switchActivityIntent.putExtra("User", user);
        startActivity(switchActivityIntent);
    }


    private void populatework(){
        Work work = getWork();
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
                Log.i("user", nome);
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