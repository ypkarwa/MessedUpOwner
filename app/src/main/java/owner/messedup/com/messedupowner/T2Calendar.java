package owner.messedup.com.messedupowner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import owner.messedup.com.messedupowner.data.model.DateStatusMenu;
import owner.messedup.com.messedupowner.data.model.MessMenu;
import owner.messedup.com.messedupowner.data.model.Messweek;
import owner.messedup.com.messedupowner.data.model.WeekMenu;
import owner.messedup.com.messedupowner.data.model.WeekStatus;
import owner.messedup.com.messedupowner.data.model.isSuccess;
import owner.messedup.com.messedupowner.data.remote.APIManager;
import owner.messedup.com.messedupowner.data.remote.APIService;
import static owner.messedup.com.messedupowner.Const.OWNER_PREFS;

public class T2Calendar extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private CompositeDisposable disposable = new CompositeDisposable();
    SharedPreferences sp;
    APIService apiService;
    SwipeRefreshLayout srl;

    String fromServerDate;
    WeekStatus fromServerweekStatus;
    List<WeekMenu> fromServerweekMenu;




    SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM, yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
    SimpleDateFormat sdfortime = new SimpleDateFormat("hhmm");
    SimpleDateFormat sdfortimeaa = new SimpleDateFormat("aa");
    Date newDate, minDate, maxDate;
    int timecheck;
    String timecheckaa;
    int dayName;


    CompactCalendarView Calendar;
    TextView Month;
    TextView lview,dview;
    Event lunch_event,dinner_event;
    String dayOfTheWeek,mydate,dayforDBH;
    TextView today;
    long oneday = 24*60*60*1000;
    long fpfhour = 5*60*60*1000+30*60*1000;

    long mind,maxd,nextmonth;
    Button LunchButton,DinnerButton,OfferButton;
    boolean status[][];


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences(OWNER_PREFS, Context.MODE_PRIVATE);


        apiService = APIManager.getClient(getActivity().getApplicationContext())
                .create(APIService.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_t2_calendar,container,false);
        Log.i("T2Calendar","onCreateView");

        srl = rootView.findViewById(R.id.swipe_refresh_layout);
        srl.setOnRefreshListener(this);

        today = (TextView) rootView.findViewById(R.id.textView7);
        lview = (TextView) rootView.findViewById(R.id.textView2);
        dview = (TextView) rootView.findViewById(R.id.textView3);

        LunchButton = (Button) rootView.findViewById(R.id.button2);
        DinnerButton = (Button) rootView.findViewById(R.id.button);

        Calendar = (CompactCalendarView) rootView.findViewById(R.id.compactcalendar_view);
        Calendar.setUseThreeLetterAbbreviation(true);

        getDate();
        //getWeekStatus();
        //getMenu();
        return rootView;
    }



    @Override
    public void onRefresh()
    {
        getDate();
        //getWeekStatus();
        //getMenu();
        srl.setRefreshing(false);
        //setCalender();
    }

    private void getWeekStatus() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("messid",sp.getString("MessId","error"));
        disposable.add(
                apiService.fetchWeekStatus(hashMap)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<WeekStatus>() {
                            @Override
                            public void onSuccess(WeekStatus weekStatus) {
                                fromServerweekStatus = weekStatus;
                                Log.e("T2Cal","Status"+weekStatus);
                                setStatus();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("T2Cal","Error"+e.getMessage());
                            }
                        })



        );
    }

    private void getMenu() {
        disposable.add(
                apiService.getWeekMenu(sp.getString("MessName","Error"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<WeekMenu>>() {
                            @Override
                            public void onSuccess(List<WeekMenu> weekMenu) {
                                fromServerweekMenu = weekMenu;
                                Log.e("T2Cal","Menu"+weekMenu.toString());
                                setMenu();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("T2Cal","Error"+e.getMessage());
                            }
                        })
        );
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
                                setCalender();
                                Log.e("T2Cal","Date :" + date);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("T2Cal","DateError"+e.getMessage());
                            }
                        })
        );
    }





    private void setCalender() {

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
        maxd = (newDate.getTime() + 6 * oneday) - ((newDate.getTime() + 6 * oneday) % (oneday)) - fpfhour;
        nextmonth = (newDate.getTime() + 29 * oneday) - ((newDate.getTime() + 29 * oneday) % (oneday)) - fpfhour;
        minDate = new Date(mind);
        maxDate = new Date(maxd);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE-dd");
        String tod = simpleDateFormat.format(minDate);

        String todaytext = "Today : ";
        SpannableString str = new SpannableString(todaytext + tod);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, todaytext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        today.setText(str);


        dayName = minDate.getDay();

        Log.e("minDate Day", String.valueOf(dayName));

        Calendar.setVisibility(View.VISIBLE);

        Month = (TextView) getView().findViewById(R.id.textview);
        Month.setText(dateFormatForMonth.format(Calendar.getFirstDayOfCurrentMonth()));
        Month.setVisibility(View.VISIBLE);


        getWeekStatus();
        getMenu();
    }

    private void setStatus() {

        status = getFlagWeekMenu();
        Calendar.removeAllEvents();


        int startnumber = dayName;
        for (int i = 0; i < 7; i++) {
            long setDate = (newDate.getTime() + i * oneday) - ((newDate.getTime() + i * oneday) % (oneday) - fpfhour);
            if (status[startnumber % 7][0])
                lunch_event = new Event(Color.GREEN, setDate, "Lunch");
            else
                lunch_event = new Event(Color.LTGRAY, setDate, "NoLunch");

            if (status[startnumber % 7][1])
                dinner_event = new Event(Color.GREEN, setDate, "Dinner");
            else
                dinner_event = new Event(Color.LTGRAY, setDate, "NoDinner");

            startnumber++;

            Calendar.addEvent(lunch_event, true);
            Calendar.addEvent(dinner_event, true);
        }

    }


    private void setMenu(){



        Calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                Log.e("Date", dateClicked.toString());
                Log.e("Date", minDate.toString());
                Log.e("Date", maxDate.toString());

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

                dayOfTheWeek = sdf.format(dateClicked);

                SimpleDateFormat sdfweek = new SimpleDateFormat("EEE");

                dayforDBH = sdfweek.format(dateClicked).toUpperCase();
                Log.i("day for dbh", dayforDBH);

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                mydate = df.format(dateClicked);

                if ((dateClicked.after(minDate) || dateClicked.equals(minDate)) && (dateClicked.before(maxDate) || dateClicked.equals(maxDate))) {
                    int daynum = dateClicked.getDay();

                    if (!(timecheck >= 300 && timecheckaa.equals("pm") && dateClicked.equals(minDate))) {
                        LunchButton.setClickable(true);

                        if (status[daynum][0]) {
                            MessMenu m = null;
                            for(WeekMenu weekmenu : fromServerweekMenu)
                            {
                                if(weekmenu.getDay().equals(dayforDBH))
                                    m = weekmenu.getMenu().get(1);
                            }

                            String boldText = "Lunch Set : ";
                            String normalText = m.toString();
                            SpannableString str = new SpannableString(boldText + normalText);
                            str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            lview.setText(str);
                        } else {
                            lview.setText("Lunch Not Set");
                        }

                        LunchButton.setBackground(getResources().getDrawable(R.drawable.lun_din_bg));
                    } else {
                        LunchButton.setClickable(false);
                        DinnerButton.setClickable(false);
                        LunchButton.setBackground(getResources().getDrawable(R.drawable.button_grey));
                        DinnerButton.setBackground(getResources().getDrawable(R.drawable.button_grey));

                        lview.setText("Sorry not available");
                        dview.setText("Sorry not available");

                    }

                    if (!(timecheck >= 1100 && timecheckaa.equals("pm") && dateClicked.equals(minDate))) {
                        DinnerButton.setClickable(true);
                        if (status[daynum][1]) {
                            MessMenu m = null;
                            for(WeekMenu weekmenu : fromServerweekMenu)
                            {
                                if(weekmenu.getDay().equals(dayforDBH))
                                    m = weekmenu.getMenu().get(0);
                            }
                            String boldText = "Dinner Set : ";
                            String normalText = m.toString();
                            SpannableString str = new SpannableString(boldText + normalText);
                            str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            dview.setText(str);

                        } else
                            dview.setText("Dinner Not Set");
                        DinnerButton.setBackground(getResources().getDrawable(R.drawable.lun_din_bg));
                    } else {
                        LunchButton.setClickable(false);
                        DinnerButton.setClickable(false);
                        LunchButton.setBackground(getResources().getDrawable(R.drawable.button_grey));
                        DinnerButton.setBackground(getResources().getDrawable(R.drawable.button_grey));

                        lview.setText("Sorry not available");
                        dview.setText("Sorry not available");

                    }
                } else {
                    LunchButton.setClickable(false);
                    DinnerButton.setClickable(false);
                    LunchButton.setBackground(getResources().getDrawable(R.drawable.button_grey));
                    DinnerButton.setBackground(getResources().getDrawable(R.drawable.button_grey));

                    lview.setText("Sorry not available");
                    dview.setText("Sorry not available");


                }
            }



            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Month.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                mydate=null;
                dayOfTheWeek=null;
            }

        });


        LunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(inetcheck()) {
                    Intent intent = new Intent(getActivity().getBaseContext(), Upload.class);
                    if(dayOfTheWeek==null || mydate==null)
                    {
                        Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        MessMenu m = null;
                        for(WeekMenu weekmenu : fromServerweekMenu)
                        {
                            if(weekmenu.getDay().equals(dayOfTheWeek.substring(0,3).toUpperCase()))
                                m = weekmenu.getMenu().get(1);
                        }
                        Log.e("InCalender",m.toString());
                        intent.putExtra("menuobj", m);
                        intent.putExtra("meal", "Lunch");
                        intent.putExtra("day", dayOfTheWeek);
                        intent.putExtra("date", mydate);

                        getActivity().startActivity(intent);
                    }

                }
                else
                {
                    Toast.makeText(getActivity(), "Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inetcheck()) {
                    if(dayOfTheWeek==null || mydate==null)
                    {
                        Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        MessMenu m = null;
                        for(WeekMenu weekmenu : fromServerweekMenu)
                        {
                            if(weekmenu.getDay().equals(dayOfTheWeek.substring(0,3).toUpperCase()))
                                m = weekmenu.getMenu().get(0);
                        }

                        Log.e("InCalender",m.toString());
                        Log.e("InCalender",dayOfTheWeek);
                        Log.e("InCalender",dayforDBH);

                        Intent intent = new Intent(getActivity(), Upload.class);
                        intent.putExtra("menuobj", m);
                        intent.putExtra("meal", "Dinner");
                        intent.putExtra("day", dayOfTheWeek);
                        intent.putExtra("date", mydate);

                        startActivity(intent);
                    }
                }

                else
                {
                    Toast.makeText(getActivity(), "Connect to Internet", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }




    private boolean[][] getFlagWeekMenu() {
        boolean status[][] = new boolean[8][2];
        Messweek messweekList = fromServerweekStatus.getMessweek().get(0);

        status[0][0] = messweekList.getLunsun().equals("1");
        status[1][0] = messweekList.getLunmon().equals("1");
        status[2][0] = messweekList.getLuntue().equals("1");
        status[3][0] = messweekList.getLunwed().equals("1");
        status[4][0] = messweekList.getLunthu().equals("1");
        status[5][0] = messweekList.getLunfri().equals("1");
        status[6][0] = messweekList.getLunsat().equals("1");

        status[0][1] = messweekList.getDinsun().equals("1");
        status[1][1] = messweekList.getDinmon().equals("1");
        status[2][1] = messweekList.getDintue().equals("1");
        status[3][1] = messweekList.getDinwed().equals("1");
        status[4][1] = messweekList.getDinthu().equals("1");
        status[5][1] = messweekList.getDinfri().equals("1");
        status[6][1] = messweekList.getDinsat().equals("1");
        return status;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MainApplication.getRefWatcher(getActivity()).watch(this);
        Log.e("T2CAl","Disposed WeekStatus + Calender");
        disposable.dispose();
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
}
