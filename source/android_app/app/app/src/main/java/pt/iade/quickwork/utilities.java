package pt.iade.quickwork;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import pt.iade.quickwork.DownloadTasks.JSONarraydownloadtask;
import pt.iade.quickwork.models.User;
import pt.iade.quickwork.models.Work;

public class utilities {

    public static User populate_usr(JSONObject object){
        if(object != null){
            int id;
            String name;
            String email;
            int jobn;
            Integer rating= null;
            User user;
            try {
                id = object.getInt("id");
                name= object.getString("name");
                email = object.getString("email");
                jobn = object.getInt("jobnumber");
                if(!object.isNull("rating")) {
                    rating = object.getInt("rating");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            user = new User(id, name, email, jobn, rating);
            Log.i("Populate_usr", user.getName());
            return user;
        }
        else return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Work populate_work(JSONObject object){
        if (object!= null){
            Work work;
            int id;
            double pricehr;
            Double tip = null;
            LocalDate started= null;
            LocalDate finished = null;
            String type;
            double lat;
            double lon;
            try {
                id = object.getInt("id");
                Log.i("worek", ""+object.getInt("id"));
                pricehr = object.getDouble("pricehr");
                type = object.getString("type");
                if(!object.isNull("started_time")){
                    started = LocalDate.parse(object.getString("started_time"));
                }
                if(!object.isNull("finished_time")){
                    finished = LocalDate.parse(object.getString("finished_time"));
                }
                if(finished != null){
                    tip = object.getDouble("tip");
                }
                lat = object.getDouble("lat");
                lon = object.getDouble("lon");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            work = new Work(id, pricehr, tip, started, finished, type, lat, lon);
            return work;

        }




        return null;
    }



    public static JSONObject Work_JSON(Work work){

        ArrayList<String> strtype = new ArrayList<>();
        ArrayList<Integer> type_id = new ArrayList<>();
        JSONArray types = new JSONArray();
        JSONarraydownloadtask task = new JSONarraydownloadtask();
        try {
            types = task.execute(Constants.api_server+"worktype").get();
            task.cancel(true);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < types.length(); i++){
            try {
                JSONObject jsonpart = types.getJSONObject(i);
                strtype.add(jsonpart.getString("name"));
                type_id.add(jsonpart.getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.i("type", type_id.get(0)+"");

        JSONObject workobj = new JSONObject();
        String type;
        type = work.getType();
        Log.i("worktype", work.getType());
        try {
            for(int i = 0; i< strtype.size(); i++){
                if(Objects.equals(type, strtype.get(i))) {
                    workobj.put("typeid", type_id.get(i));
                }
            }
            workobj.put("pricehr",work.getPricehr());
            workobj.put("lat",work.getLat());
            workobj.put("lon",work.getLon());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return workobj;
    }

}
