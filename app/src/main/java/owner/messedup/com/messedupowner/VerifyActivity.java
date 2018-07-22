package owner.messedup.com.messedupowner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import owner.messedup.com.messedupowner.data.model.OwnerData;
import owner.messedup.com.messedupowner.data.model.Verification;
import owner.messedup.com.messedupowner.data.model.isSuccess;
import owner.messedup.com.messedupowner.data.remote.APIManager;
import owner.messedup.com.messedupowner.data.remote.APIService;

import static owner.messedup.com.messedupowner.Const.OWNER_PREFS;

public class VerifyActivity extends AppCompatActivity {

    private CompositeDisposable disposable = new CompositeDisposable();
    SharedPreferences sp;
    APIService apiService;
    boolean doubleBackToExitPressedOnce = false;
    ProgressBar progressBar;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        apiService = APIManager.getClient(getApplicationContext())
                .create(APIService.class);

        if(inetcheck()) {
            verifyfunction();
        }
        else
        {
            Toasty.error(VerifyActivity.this,"No Internet").show();

        }
        findViewById(R.id.btnLogoutVerify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getSharedPreferences(OWNER_PREFS,MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply();
                finish();
                startActivity(new Intent(VerifyActivity.this, PhoneNumberAuthentication.class));

            }
        });

        findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inetcheck()) {
                    verifyfunction();
                }
                else
                {
                    Toasty.error(VerifyActivity.this,"No Internet").show();

                }
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void verifyfunction()
    {
        disposable.add(

                apiService.getVerficationDetails(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Verification>() {
                            @Override
                            public void onSuccess(Verification verification) {
                                progressBar.setVisibility(View.GONE);
                                Log.d("VerifyDipak",verification.toString());


                                if(verification.getSuccess().equals("1")) {
                                    if (verification.getVerified().equals("1")) {
                                        Log.d("Verify",verification.toString());

                                        Toasty.success(VerifyActivity.this, "Welcome " + verification.getOwnername()+" !").show();

                                        setSharedPreferences(verification);

                                        saveUserData(new OwnerData(
                                                FirebaseAuth.getInstance().getUid(),
                                                verification.getMessname(),
                                                verification.getOwnername(),
                                                verification.getPhonenumber(),
                                                verification.getArea()
                                        ));
                                        //startActivity(new Intent(VerifyActivity.this, MainActivity.class));

                                    } else {
                                        Log.d("Verify",verification.toString());

                                        Toasty.success(VerifyActivity.this, verification.getOwnername()+ " in Process").show();

                                    }
                                }
                                else
                                {
                                    Toasty.error(VerifyActivity.this, ""+verification.getMessage()).show();

                                    if (verification.getMessage().equals("Not Registered")) {
                                        finish();
                                        startActivity(new Intent(VerifyActivity.this, FillDetails.class));
                                    }
                                    else
                                    {
                                        Toast.makeText(VerifyActivity.this,"Try Login Again",Toast.LENGTH_LONG).show();
                                    }

                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                progressBar.setVisibility(View.GONE);
                                Log.e("VerifyActivityFailure",e.getMessage());

                                Toasty.error(VerifyActivity.this,"Could Not Verify - Failed").show();
                            }
                        }));
    }

    private void saveUserData(OwnerData owner)
    {
        disposable.add(
                apiService
                        .saveOwner(owner)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.e("Dipak","DOne");

                                startActivity(new Intent(VerifyActivity.this, MainActivity.class));

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(VerifyActivity.this,
                                        "Error : " + "Could not add, Try Again"
                                        , Toast.LENGTH_LONG).show();
                                Log.e("Verify Activity", "onError: " + e.getMessage());


                                //startActivity(new Intent(FillDetails.this, VerifyActivity.class));
                                //showError(e);
                            }
                        }));
    }

    private void setSharedPreferences(Verification ownerData) {
        sp = getSharedPreferences(OWNER_PREFS, Context.MODE_PRIVATE);
        sp.edit().putString("MessId",FirebaseAuth.getInstance().getUid()).apply();
        sp.edit().putString("MessName",ownerData.getMessname()).apply();
        sp.edit().putString("OwnerName",ownerData.getOwnername()).apply();
        sp.edit().putString("PhoneNumber",ownerData.getPhonenumber()).apply();
        sp.edit().putString("Area",ownerData.getArea()).apply();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    /*private boolean isSharedPreferencesCorrect()
    {
        sp = getSharedPreferences(OWNER_PREFS, Context.MODE_PRIVATE);
        if(sp.getString("MessName","null45").equals("null45") ||
                sp.getString("OwnerName","null45").equals("null45") ||
                sp.getString("Area","null45").equals("null45") ||
                sp.getString("PhoneNumber","null").equals("null")) {
            return false;
        }

        return true;
    }*/

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}
