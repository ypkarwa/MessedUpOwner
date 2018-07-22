package owner.messedup.com.messedupowner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;

import static owner.messedup.com.messedupowner.Const.OWNER_PREFS;

public class PhoneNumberAuthentication extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        if(!inetcheck()){
            Toasty.error(PhoneNumberAuthentication.this,"No Internet").show();
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();
        getSharedPreferences(OWNER_PREFS, MODE_PRIVATE)
                .edit()
                .putInt("isStartPoint", 1)
                .apply();
        /*getSharedPreferences(OWNER_PREFS,MODE_PRIVATE)
                .edit()
                .clear()
                .apply();*/
        if (auth.getCurrentUser() != null) {
            // already signed in
            Log.e("LoginCheck","Phone Number : "+auth.getCurrentUser().getPhoneNumber());

            Intent i =  new Intent(PhoneNumberAuthentication.this, VerifyActivity.class);
            //i.putExtra("PhoneNumber",auth.getCurrentUser().getPhoneNumber());
            startActivity(i);
            finish();
        } else {
            // not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.FirebaseTheme)
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.PhoneBuilder().build()
                                    ))
                            .build(),
                    RC_SIGN_IN);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {

                startActivity(new Intent(PhoneNumberAuthentication.this,VerifyActivity.class));
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e("Login","Login cancelled by User");
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e("Login","No Internet Connection");
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login","Unknown Error");
                    return;
                }
            }
            Log.e("Login","Unknown sign in response");
        }
    }

    boolean inetcheck()
    {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;
    }


}