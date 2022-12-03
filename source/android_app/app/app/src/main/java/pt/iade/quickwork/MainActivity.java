package pt.iade.quickwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pt.iade.quickwork.downloadTask.JSONarraydownloadtask;
import pt.iade.quickwork.models.User;

public class MainActivity extends AppCompatActivity {

    JSONArray arrayWorks = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        JSONarraydownloadtask task = new JSONarraydownloadtask();
        try {
            Log.i("JOBS", "started ");
            arrayWorks = task.execute(Constants.api_server +"work").get();

        } catch (ExecutionException e) {
            Log.i("JOBS", "failed ");
            e.printStackTrace();
            arrayWorks = null;
        } catch (InterruptedException e) {
            Log.i("JOBS", "failed 2 ");
            e.printStackTrace();
            arrayWorks = null;
        }
        Populateworklistview();

    }

    //switch between activities


    private void Populateworklistview(){
        if(arrayWorks != null) {

            ListView mylist = (ListView) findViewById(R.id.WorkListView);
            ArrayList<String> myItems = new ArrayList<>();
            for (int i = 0; i < arrayWorks.length(); i++) {
                try {

                    JSONObject jsonPart = arrayWorks.getJSONObject(i);
                    myItems.add(jsonPart.getString("id")+" "+jsonPart.getString("type")+" lat: "+ jsonPart.getString("lat") + " lon: "+ jsonPart.getString("lon") );
                }catch ( Exception e){
                    e.printStackTrace();
                }
            }

            ArrayAdapter<String> myListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myItems);
            mylist.setAdapter(myListAdapter);


            createListViewClickItemEvent(mylist, myItems);

        }

    }
    //send information betweenactivities(ex: sending the work id to activity_workview)
    private void switchtoworkview(String id) {
        Intent switchActivityIntent = new Intent(this, Workview.class);
        switchActivityIntent.putExtra("workid", id);
        startActivity(switchActivityIntent);
    }

    //know when a job from the list is pressed
    private void createListViewClickItemEvent(ListView list, final ArrayList<String> items)
    {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String work_info = items.get(position);
                String parts[] = work_info.split(" ");
                Integer ID = Integer.valueOf(parts[0]);
                switchtoworkview(parts[0]);


                Log.i("listviewclick", "Item: " + parts[0]+ " clicked");
            }
        });
    }
}