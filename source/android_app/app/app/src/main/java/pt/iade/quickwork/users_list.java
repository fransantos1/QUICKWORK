package pt.iade.quickwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pt.iade.quickwork.DownloadTasks.JSONarraydownloadtask;
import pt.iade.quickwork.DownloadTasks.JSONobjdownloadtask;
import pt.iade.quickwork.models.User;

public class users_list extends AppCompatActivity {

    JSONArray arrayUsers = null;
    private ArrayList<Integer> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_userslistview);
        Log.i("VIEW", "changed to debug view");
        JSONarraydownloadtask task = new JSONarraydownloadtask();
        try {
            arrayUsers =  task.execute(Constants.api_server +"users").get();

            task.cancel(true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printarray(arrayUsers);
        Populatelistview();
    }
    private void printarray(JSONArray array){
        Log.i("print array", array.toString());

    }



    private void Populatelistview() {
        if (arrayUsers != null) {
            ListView mylist = (ListView) findViewById(R.id.USERS_LISTVIEW);
            ArrayList<String> myItems = new ArrayList<>();
            idList = new ArrayList<>();
            Log.i("lenght", arrayUsers.length()+"");
            for (int i = 0; i < arrayUsers.length(); i++) {
                try {
                    JSONObject jsonPart = arrayUsers.getJSONObject(i);
                    idList.add(jsonPart.getInt("id"));
                    myItems.add(jsonPart.getString("name"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ArrayAdapter<String> myListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myItems);
            mylist.setAdapter(myListAdapter);


            createListViewClickItemEvent(mylist, myItems);

        }
    }
    private void createListViewClickItemEvent(ListView list, final ArrayList<String> items){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idnumber = idList.get(position);
                Log.i("id selected", ""+idnumber);
                User user = getUser(idnumber);
                switchtactivity(user);

            }
        });
    }


    //Switch to the next activity, need to verify whether or not the user is in an active job
    private void switchtactivity(User user){
        //getting the boolean to see if the user is occupied
        JSONObject jsonObject = null;
        JSONobjdownloadtask tak = new JSONobjdownloadtask();
        boolean isoccupied = false;
        boolean isowner = false;
        try {
            jsonObject = tak.execute(Constants.api_server +"users/"+user.getId()+"/occupied").get();
            tak.cancel(true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(jsonObject== null){

            Toast.makeText(this, "Aconteceu um erro a tentar iniciar", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            isoccupied = jsonObject.getBoolean("haswork");
            isowner = jsonObject.getBoolean("isowner");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //changing to different activities
        if(isoccupied){
            if(isowner){
                //change to owners activity
                Intent switchActivityIntent = new Intent(this, Job_creator.class);
                switchActivityIntent.putExtra("User", user);
                startActivity(switchActivityIntent);
                Log.i("user", "is occuppied");



                Log.i("user", "is occuppied and has work");
            }else {
                //change to worker activity and send User
                Intent switchActivityIntent = new Intent(this, Job_worker.class);
                switchActivityIntent.putExtra("User", user);
                startActivity(switchActivityIntent);
                Log.i("user", "is occuppied");
            }
        }else {
            //change to map activity
            Intent switchActivityIntent = new Intent(this, jobsmap.class);
            switchActivityIntent.putExtra("User", user);
            startActivity(switchActivityIntent);
            Log.i("user", "is not occuppied");
        }

    }

    private User getUser(int id){
        JSONobjdownloadtask jsoNobjdownloadtask = new JSONobjdownloadtask();
        JSONObject userinfo = null;
        User user = null;
        try {
            userinfo = jsoNobjdownloadtask.execute(Constants.api_server +"users/"+id).get();
            jsoNobjdownloadtask.cancel(true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (userinfo!= null) {
            user = utilities.populate_usr(userinfo);
        }

        return user;
    }



}




