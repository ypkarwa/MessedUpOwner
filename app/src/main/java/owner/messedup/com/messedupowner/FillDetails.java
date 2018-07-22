package owner.messedup.com.messedupowner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.leakcanary.LeakCanary;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import owner.messedup.com.messedupowner.data.model.OwnerData;
import owner.messedup.com.messedupowner.data.model.isSuccess;
import owner.messedup.com.messedupowner.data.remote.APIManager;
import owner.messedup.com.messedupowner.data.remote.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static owner.messedup.com.messedupowner.Const.OWNER_PREFS;

public class FillDetails extends AppCompatActivity {

    EditText etMessName, etOwnerName, etPhoneNumber, etArea;

    private ProgressBar spinner;

    private CompositeDisposable disposable = new CompositeDisposable();

    private APIService apiService;

    private OwnerData owner;

    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fill_details);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        etMessName = findViewById(R.id.etMessName);
        etOwnerName = findViewById(R.id.etOwnerName);
        etPhoneNumber = findViewById(R.id.etMobileNumber);
        etArea = findViewById(R.id.etCollegeArea);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);


        etArea.setText("PICT, BVP, Katraj");
        etArea.setInputType(InputType.TYPE_NULL);
        etArea.setTextIsSelectable(false);
        etArea.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v,int keyCode,KeyEvent event) {
                return true;  // Blocks input from hardware keyboards.
            }
        });

        etPhoneNumber.setText(auth.getCurrentUser().getPhoneNumber());
        etPhoneNumber.setInputType(InputType.TYPE_NULL);
        etPhoneNumber.setTextIsSelectable(false);
        etPhoneNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v,int keyCode,KeyEvent event) {
                return true;  // Blocks input from hardware keyboards.
            }
        });



        apiService = APIManager.getClient(getApplicationContext())
                .create(APIService.class);

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                owner = new OwnerData(
                        FirebaseAuth.getInstance().getUid(),
                        etMessName.getText().toString(),
                        etOwnerName.getText().toString(),
                        etPhoneNumber.getText().toString(),
                        etArea.getText().toString());

                spinner.setVisibility(View.VISIBLE);
                registerUser();

            }
        });

        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                        .edit()
                        .clear()
                        .apply();
                startActivity(new Intent(FillDetails.this, PhoneNumberAuthentication.class));

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private void registerUser() {


        disposable.add(
                apiService
                        .createOwner(owner)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<isSuccess>() {
                            @Override
                            public void onSuccess(isSuccess s) {
                                spinner.setVisibility(View.GONE);

                                if(s.getSuccess().equals("0") && s.getMessage().equals("23000"))
                                {
                                    Toasty.error(FillDetails.this,
                                            "User Exists").show();

                                }

                                if(s.getSuccess().equals("1")){
                                Toast.makeText(FillDetails.this,
                                        "Owner Added",
                                        Toast.LENGTH_LONG)
                                        .show();

                                startActivity(new Intent(FillDetails.this, VerifyActivity.class));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(FillDetails.this,
                                        "Error is " + e.getMessage()
                                        , Toast.LENGTH_LONG).show();
                                Log.e("FillDetails", "onError: " + e.getMessage());

                                PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                                        .edit()
                                        .clear()
                                        .apply();
                                //startActivity(new Intent(FillDetails.this, VerifyActivity.class));
                                //showError(e);
                            }
                        }));
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


/*MainApplication.apiManager.createOwner(owner, new Callback<OwnerData>() {
                    @Override
                    public void onResponse(Call<OwnerData> call, Response<OwnerData> response) {
                        spinner.setVisibility(View.GONE);
                        OwnerData responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {
                            Toast.makeText(FillDetails.this,"Owner Added",
                                    Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            Toast.makeText(FillDetails.this,
                                    String.format("Response is %s", String.valueOf(response.code()))
                                    , Toast.LENGTH_LONG).show();
                            Log.e("FillDetails", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<OwnerData> call, Throwable t) {
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(FillDetails.this,
                                "Error is " + t.getMessage()
                                , Toast.LENGTH_LONG).show();
                        Log.e("FillDetails", String.valueOf(t.getMessage()));

                    }
                });*/