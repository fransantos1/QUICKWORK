package pt.iade.quickwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        button = (Button) findViewById(R.id.button_Switchmap);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch_usersview();
            }
        });
    }
/*

    //send information betweenactivities(ex: sending the work id to activity_workview)

    private void switchtoworkview(String id) {
        Intent switchActivityIntent = new Intent(this, Workview.class);
        switchActivityIntent.putExtra("workid", id);
        startActivity(switchActivityIntent);
    }
*/
    private void switch_usersview() {
        Intent switchActivityIntent = new Intent(this, users_list.class);
        startActivity(switchActivityIntent);
    }
}