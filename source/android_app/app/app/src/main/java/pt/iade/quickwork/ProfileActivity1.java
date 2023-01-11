package pt.iade.quickwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import pt.iade.quickwork.models.User;

public class ProfileActivity1 extends AppCompatActivity {
    User user = null;
    TextView Nome, email,ntrabalhos,rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);

        Nome = (TextView) (findViewById(R.id.textviewnome));
        email = (TextView) (findViewById(R.id.textviewemail));
        ntrabalhos = (TextView) (findViewById(R.id.textviewntrabalhos));
        rating = (TextView) (findViewById(R.id.textviewrating));
        
        user = (User) getIntent().getSerializableExtra("User");
        if (user != null ){
        Log.i("test", user.getName());
        Profiletext();
        }else Log.w("USER", "USER VALUE IS NULL");

    }



    public void Profiletext() {




        String usrnome = user.getName();
        String usrmail = user.getEmail();
        String usrntrabalho = Integer.toString(user.getJobnumber());
        if (user.getRating() != null) {
            String usrrating = Integer.toString(user.getRating());
            rating.setText(usrrating);
        }else {
            rating.setText("User not rated");
        }


        Nome.setText(usrnome);
        email.setText(usrmail);
        ntrabalhos.setText(usrntrabalho);



    }
}
