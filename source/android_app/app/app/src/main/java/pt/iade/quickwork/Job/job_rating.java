package pt.iade.quickwork.Job;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pt.iade.quickwork.Constants;
import pt.iade.quickwork.DownloadTasks.JSONarraydownloadtask;
import pt.iade.quickwork.DownloadTasks.JSONobjdownloadtask;
import pt.iade.quickwork.DownloadTasks.PostTask;
import pt.iade.quickwork.R;
import pt.iade.quickwork.jobsmap;
import pt.iade.quickwork.models.User;
import retrofit2.http.POST;

public class job_rating extends AppCompatActivity {
    int rating, index = 0;
    JSONObject ratingobj;
    String comment;
    Button upload;
    RatingBar ratingbar;
    TextInputLayout inputcomment;
    TextView name;
    User loggeduser;
    ArrayList<String> usrnames = new ArrayList<>();
    ArrayList<Integer> uwids = new ArrayList<>();
    JSONArray uw = new JSONArray();
    JSONObject uwobj = new JSONObject();
    JSONarraydownloadtask uwarray = new JSONarraydownloadtask();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_rating);
        loggeduser = (User) getIntent().getSerializableExtra("User");
        try {
            uw = uwarray.execute(Constants.api_server+"comment/usrwork/"+loggeduser.getId()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        for(int j = 0; j <= uw.length()-1;j++){
            try {
                usrnames.add(uw.getJSONObject(j).getString("name"));
                uwids.add(uw.getJSONObject(j).getInt("uw_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        ratingbar = findViewById(R.id.ratingBar_rating);
        upload = (Button)  findViewById(R.id.button_finished);
        inputcomment = findViewById(R.id.textInputLayout);
        name = findViewById(R.id.textView_ratingname);
        name.setText(usrnames.get(index));
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateJson();
            }
        });
    }

    public void populateJson(){
        /*
{
        "comment":"to alho2!!",
        "rating":11,
        "ratingusrid": 14,
        "ratedusrwork": 59
}
*/
        ratingobj = new JSONObject();
        rating = (int) ratingbar.getRating()*2;
        if (rating == 0){
            Toast.makeText(this, "por favor introduza uma rating", Toast.LENGTH_SHORT).show();
            return;
        }
        comment = inputcomment.getEditText().getText().toString();
        Log.i("input text", comment);
        try {
            ratingobj.put("comment", comment);
            ratingobj.put("rating", rating);
            ratingobj.put("ratingusrid", loggeduser.getId());
            ratingobj.put("ratedusrwork", uwids.get(index));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Post();
    }
    public void Post(){
        PostTask task = new PostTask();
        int response = 0;

        try {
            response = task.execute(Constants.api_server+"comment", ratingobj.toString()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if(response == 200){
            ClearandChange();
        }else{Toast.makeText(this, "ocorreu um erro a comentar", Toast.LENGTH_SHORT).show();}

    }
    public void ClearandChange(){
        ratingobj = null;
        if(index == uwids.size()-1){
            Intent switchActivityIntent = new Intent(this, jobsmap.class);
            switchActivityIntent.putExtra("User", loggeduser);
            startActivity(switchActivityIntent);
            return;
        }
        Log.i("size", uwids.size()+"  "+ index);
        index = index+1;
        name.setText(usrnames.get(index));

    }


}


