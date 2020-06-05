package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private final static int SPLASH_TIME = 1000;
    private Intent activityIntent;
    FirebaseAuth firebaseAuth;
    private boolean isLoggedin= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth= FirebaseAuth.getInstance();
        isLoggedin = firebaseAuth.getCurrentUser() != null;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLoggedin) activityIntent = new Intent(SplashActivity.this, MainActivity.class);
                else activityIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(activityIntent);
                finish();
            }
        }, SPLASH_TIME);
    }
}
