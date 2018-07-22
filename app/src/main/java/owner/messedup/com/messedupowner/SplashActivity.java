package owner.messedup.com.messedupowner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static owner.messedup.com.messedupowner.Const.OWNER_PREFS;

public class SplashActivity extends AppCompatActivity {

    Integer isStartPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        isStartPoint = getSharedPreferences(OWNER_PREFS, MODE_PRIVATE)
                .getInt("isStartPoint", 0);


    }

    @Override
    protected void onResume() {
        super.onResume();

        SplashActivity.this.finish();

        if (isStartPoint.equals(2)) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        else if(isStartPoint.equals(1)){
            startActivity(new Intent(SplashActivity.this, PhoneNumberAuthentication.class));
        }
        else{
            startActivity(new Intent(SplashActivity.this, IntroActivity.class));
        }


    }

}