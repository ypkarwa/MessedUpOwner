package owner.messedup.com.messedupowner;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import owner.messedup.com.messedupowner.MyAdapter.MyScanAdapter;
import owner.messedup.com.messedupowner.data.model.Lastscan;
import owner.messedup.com.messedupowner.data.model.Statistics;
import owner.messedup.com.messedupowner.data.model.Usercount;
import owner.messedup.com.messedupowner.data.model.WeekMenu;
import owner.messedup.com.messedupowner.data.remote.APIManager;
import owner.messedup.com.messedupowner.data.remote.APIService;

import static owner.messedup.com.messedupowner.Const.OWNER_PREFS;

public class T1Stats extends Fragment{

    private CompositeDisposable disposable = new CompositeDisposable();
    SharedPreferences sp;
    APIService apiService;

    String probableusers;
    List<Lastscan> lastscans;
    List<Usercount> usercounts;

    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM");

    private ListView listView;
    private MyScanAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences(OWNER_PREFS, Context.MODE_PRIVATE);
        apiService = APIManager.getClient(getActivity().getApplicationContext())
                .create(APIService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_t1_stats,container,false);
        Log.i("T1Stats","onCreateView");

        if(inetcheck()) {
            getStats();
        }
        else
        {
            Toasty.error(getActivity(),"No Internet").show();
        }

        return rootView;
    }

    private void getStats() {
        disposable.add(
                apiService.getStatistics(sp.getString("MessId","Error"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Statistics>() {
                            @Override
                            public void onSuccess(Statistics statistics) {

                                Log.e("T1Stats","Stats"+statistics.toString());
                                probableusers = statistics.getProbableusers();
                                usercounts = statistics.getUsercount();
                                lastscans = statistics.getLastscans();

                                setProbableUser();
                                setUserCount();
                                setLastScan();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("T1Stats","Error"+e.getMessage());
                            }
                        })
        );
    }

    private void setLastScan() {
        listView = getActivity().findViewById(R.id.lastscanlist);
        adapter = new MyScanAdapter(getActivity(), lastscans);
        TextView textView = new TextView(getActivity());
        textView.setText("Last 10 Scans");
        textView.setAllCaps(true);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        listView.addHeaderView(textView);
        listView.setAdapter(adapter);

    }

    private void setUserCount() {

//        Calendar calendar = Calendar.getInstance();
//        Date d1 = calendar.getTime();
//        calendar.add(Calendar.DATE, 1);
//        Date d2 = calendar.getTime();
//        calendar.add(Calendar.DATE, 1);
//        Date d3 = calendar.getTime();
//        calendar.add(Calendar.DATE, 1);
//        Date d4 = calendar.getTime();

        GraphView graph = (GraphView) getActivity().findViewById(R.id.graph);

        DataPoint dataPoints[] = new DataPoint[usercounts.size()];
        int i = 0;
        for (Usercount usercount : usercounts)
        {

                dataPoints[i] = new DataPoint(
                        new Date(Long.parseLong(usercount.getDate())*1000),
                        Integer.parseInt(usercount.getCount()));

            i++;
        }

// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        BarGraphSeries<DataPoint> series1 = new BarGraphSeries<>(dataPoints);



        graph.addSeries(series);
        graph.addSeries(series1);



        graph.getGridLabelRenderer().setLabelFormatter(
                new DefaultLabelFormatter(){
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if(isValueX){
                            return simpleDateFormat1.format(new Date((long) value));
                        }else{
                        return super.formatLabel(value, isValueX);
                    }
                    }
                });


        graph.getViewport().setMinY(0);
//        set manual x bounds to have nice steps

          graph.getViewport().setMinX(Long.parseLong(usercounts.get(0).getDate())*1000);

            graph.getViewport().setMaxX(
                    Long.parseLong(usercounts.get(usercounts.size()-1).getDate())*1000);


        series.setDrawAsPath(true);
        series.setDrawDataPoints(true);
        series.setDrawBackground(true);
        series.setDataPointsRadius(7);

        series1.setValuesOnTopSize(30);
        series1.setValuesOnTopColor(Color.rgb(0,0,0));
        series1.setDrawValuesOnTop(true);
        series1.setDataWidth(1);
        graph.setTitle("Last 7 Days Visitor Count");
        graph.setTitleTextSize(35);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 4 because of the space
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().reloadStyles();

    }

    private void setProbableUser() {
        TextView textView = getActivity().findViewById(R.id.probableusers);
        textView.setText("Probable Count for tomorrow : "+probableusers);
    }

    boolean inetcheck()
    {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
