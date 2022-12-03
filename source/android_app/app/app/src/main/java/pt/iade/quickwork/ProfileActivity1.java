package pt.iade.quickwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import pt.iade.quickwork.models.User;

public class ProfileActivity1 extends AppCompatActivity {
    User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);


        user = (User) getIntent().getSerializableExtra("User");
        if (user != null ){
        Log.i("test", user.getName());
        Profiletext();
        }else Log.w("USER", "USER VALUE IS NULL");

    }



    public void Profiletext() {


        TextView Nome = (TextView) (findViewById(R.id.textviewnome));
        TextView email = (TextView) (findViewById(R.id.textviewemail));
        TextView ntrabalhos = (TextView) (findViewById(R.id.textviewntrabalhos));
        TextView rating = (TextView) (findViewById(R.id.textviewrating));

        String usrnome = user.getName();
        String usrmail = user.getEmail();
        String usrntrabalho = Integer.toString(user.getJobnumber());
        String usrrating = Integer.toString(user.getRating());

        Nome.setText(usrnome);
        email.setText(usrmail);
        ntrabalhos.setText(usrntrabalho);
        rating.setText(usrrating);


    }
}
