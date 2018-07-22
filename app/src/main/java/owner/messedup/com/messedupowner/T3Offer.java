package owner.messedup.com.messedupowner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import owner.messedup.com.messedupowner.data.model.MessMenu;
import owner.messedup.com.messedupowner.data.model.Offer;
import owner.messedup.com.messedupowner.data.model.isSuccess;
import owner.messedup.com.messedupowner.data.remote.APIManager;
import owner.messedup.com.messedupowner.data.remote.APIService;

import static owner.messedup.com.messedupowner.Const.OWNER_PREFS;

public class T3Offer extends Fragment {

    private CompositeDisposable disposable = new CompositeDisposable();
    SharedPreferences sp;
    APIService apiService;
    Date newDate;
    String fromServerDate;

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
    SimpleDateFormat sdfortime = new SimpleDateFormat("hhmm");
    SimpleDateFormat sdfortimeaa = new SimpleDateFormat("aa");
    int timecheck;
    String timecheckaa;
    long mind,nextmonth;
    long oneday = 24*60*60*1000;
    long fpfhour = 5*60*60*1000+30*60*1000;

    String messid,offerdate,offer,response;
    Button uploadbtn;
    String today;
    EditText offerdesc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getActivity().getSharedPreferences(OWNER_PREFS, Context.MODE_PRIVATE);

        apiService = APIManager.getClient(getActivity().getApplicationContext())
                .create(APIService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_t3_offer,container,false);
        Log.i("T3Offer","onCreateView");
        getDate();
        return rootView;
    }

    private void getDate() {
        disposable.add(
                apiService.getServerDate()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<isSuccess>() {
                            @Override
                            public void onSuccess(isSuccess date) {
                                fromServerDate = date.getMessage();
                                setminmaxDate();
                                Log.e("T2Cal","Date :" + date);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("T2Cal","DateError"+e.getMessage());
                            }
                        })
        );
    }

    private void setminmaxDate() {

        try {
            if (inetcheck()) {
                newDate = sdf.parse(fromServerDate);
                timecheck = Integer.parseInt(sdfortime.format(newDate));
                timecheckaa = sdfortimeaa.format(newDate);
            } else {
                java.util.Calendar c = java.util.Calendar.getInstance();
                newDate = c.getTime();
            }

            Log.v("Setting Date", newDate.toString());
            Log.v("Time", String.valueOf(timecheck));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        mind = (newDate.getTime()) - ((newDate.getTime()) % (oneday)) - fpfhour;
        nextmonth = (newDate.getTime() + 29 * oneday) - ((newDate.getTime() + 29 * oneday) % (oneday)) - fpfhour;

        setFragmentForOperation();

    }

    private void setFragmentForOperation() {
        offerdesc = (EditText) getActivity().findViewById(R.id.offerdescription);


        uploadbtn = (Button) getActivity().findViewById(R.id.button3);

        final EditText edittext= (EditText) getActivity().findViewById(R.id.Birthday);
        edittext.setKeyListener(null);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if(myCalendar.getTimeInMillis()<mind || myCalendar.getTimeInMillis()>nextmonth)
                {
                    Toast.makeText(getActivity(), "Cannot Select", Toast.LENGTH_SHORT).show();

                }
                else {
                    String myFormat = "dd MMMM yy"; //In which you need put here
                    String myFormatforDB = "dd/MM/yy"; //In which you need put here

                    SimpleDateFormat sdf = new SimpleDateFormat(myFormatforDB, Locale.US);
                    offerdate = sdf.format(myCalendar.getTime());
                    edittext.setText(new SimpleDateFormat(myFormat,Locale.US).format(myCalendar.getTime()));
                }
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),R.style.datepicker, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dpd.setTitle("Select Offer Date");
                dpd.getDatePicker().setMinDate(mind);
                dpd.getDatePicker().setMaxDate(nextmonth);
                dpd.show();
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!inetcheck()){
                    Toasty.error(getActivity(),"No Internet").show();
                }


                offer = offerdesc.getText().toString();

                AlertDialog.Builder a_b = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);
                a_b.setMessage("Release Offer : "+offer)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(valid())
                                    posOfferToServer(new Offer(sp.getString("MessId","null"),offerdate,offer));
                                //new ReleaseOffer().execute();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = a_b.create();
                alertDialog.setTitle("Confirm");
                alertDialog.show();

            }
        });
    }
    private boolean valid() {
        boolean isvalid = true;

        if(TextUtils.isEmpty(offerdesc.getText().toString()))
        {
            offerdesc.setError("Empty!");
            isvalid = false;
        }

        return  isvalid;
    }

    private void posOfferToServer(Offer offer)
    {
        disposable.add(
                apiService
                        .postOffer(offer)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {


                                Log.e("UploadOffer","Offer Added");
                                Toast.makeText(getActivity(),"Successful",Toast.LENGTH_LONG).show();
                                clearText();



                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getActivity(),
                                        "Error : " + "Could not add, Try Again"
                                        , Toast.LENGTH_LONG).show();
                                Log.e("T3Offer", "onError: " + e.getMessage());


                                //startActivity(new Intent(FillDetails.this, VerifyActivity.class));
                                //showError(e);
                            }
                        }));
    }

    private void clearText() {

    }

    boolean inetcheck()
    {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService
                (Context.CONNECTIVITY_SERVICE);
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
    public void onDestroy() {
        super.onDestroy();
        MainApplication.getRefWatcher(getActivity()).watch(this);
        disposable.dispose();
    }
}
