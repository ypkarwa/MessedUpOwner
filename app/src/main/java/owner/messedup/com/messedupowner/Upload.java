package owner.messedup.com.messedupowner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import owner.messedup.com.messedupowner.data.model.MessMenu;
import owner.messedup.com.messedupowner.data.model.OwnerData;
import owner.messedup.com.messedupowner.data.model.isSuccess;
import owner.messedup.com.messedupowner.data.remote.APIManager;
import owner.messedup.com.messedupowner.data.remote.APIService;

import static owner.messedup.com.messedupowner.Const.OWNER_PREFS;

public class Upload extends AppCompatActivity {

    String messid,rice,roti,veg1,veg2,veg3,special,special2,other;
    String meal, dayname,getdate;
    EditText temp;
    String response;
    MessMenu m,toUploadMenuObject;
    private RadioButton mOneRadioButton;
    private RadioButton mTwoRadioButton;
    private RadioButton mThreeRadioButton;
    private RadioButton mFourRadioButton;
    private RadioButton mFiveRadioButton;

    private CompositeDisposable disposable = new CompositeDisposable();
    SharedPreferences sp;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        apiService = APIManager.getClient(getApplicationContext())
                .create(APIService.class);

        sp = getSharedPreferences(OWNER_PREFS, Context.MODE_PRIVATE);


        toUploadMenuObject = new MessMenu();
        m = (MessMenu) getIntent().getSerializableExtra("menuobj");
        Bundle bundle = getIntent().getExtras();

        if(m==null)
            Log.e("Upload","NUll Object");

        meal = bundle.getString("meal");
        dayname = bundle.getString("day");
        getdate =bundle.getString("date");

        if(meal==null)
            Log.e("Upload","meal null");

        if(dayname==null)
            Log.e("Upload","dayname null");

        Log.e("test",meal+dayname+getdate+m.toString());
        setTitle(meal+", "+getdate+" : "+dayname);

        if(m!=null)
        {

            temp = (EditText) findViewById(R.id.rice);
            if(m.getRice()!=null)
            {
                temp.setText(m.getRice());
                Log.i("MenuUploadRice",m.getRice());

            }

            temp = (EditText) findViewById(R.id.roti);
            if(m.getRoti()!=null)
            {
                temp.setText(m.getRoti());
                Log.i("MenuUploadRoti",m.getRoti());

            }
            temp = (EditText) findViewById(R.id.vegie1);
            if(m.getVegieOne()!=null)
            {
                temp.setText(m.getVegieOne());
                Log.i("MenuUploadVeg1",m.getVegieOne());

            }
            temp = (EditText) findViewById(R.id.vegie2);
            if(m.getVegieTwo()!=null)
            {
                temp.setText(m.getVegieTwo());
                Log.i("MenuUploadVeg2",m.getVegieTwo());
            }
            temp = (EditText) findViewById(R.id.vegie3);
            if(m.getVegieThree()!=null)
            {
                temp.setText(m.getVegieThree());
                Log.i("MenuUploadVeg3",m.getVegieThree());

            }
            temp = (EditText) findViewById(R.id.special);
            if(m.getSpecial()!=null)
            {
                temp.setText(m.getSpecial());
                Log.i("MenuUploadSpe",m.getSpecial());

            }
            temp = (EditText) findViewById(R.id.special2);
            if(m.getSpecialExtra()!=null)
            {
                temp.setText(m.getSpecialExtra());
                Log.i("MenuUploadSpe2",m.getSpecialExtra());

            }
            temp = (EditText) findViewById(R.id.other);
            if(m.getOther()!=null)
            {
                temp.setText(m.getOther());
                Log.i("MenuUploadOthe",m.getOther());
            }

        }

        RadioGroup rg = (RadioGroup) findViewById(R.id.radio_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                RadioButton b = (RadioButton) findViewById(checkedId);
                temp = (EditText) findViewById(R.id.roti);
                temp.setText(b.getText());
            }
        });

        mOneRadioButton = (RadioButton) findViewById(R.id.one_radio_btn);
        mTwoRadioButton = (RadioButton) findViewById(R.id.two_radio_btn);
        mThreeRadioButton = (RadioButton) findViewById(R.id.three_radio_btn);
        mFourRadioButton = (RadioButton) findViewById(R.id.four_radio_btn);
        mFiveRadioButton = (RadioButton) findViewById(R.id.two_radio_btn_1);

        final AutoCompleteTextView vegie1 =(AutoCompleteTextView) findViewById(R.id.vegie1);
        final AutoCompleteTextView vegie2 =(AutoCompleteTextView) findViewById(R.id.vegie2);
        final AutoCompleteTextView vegie3 =(AutoCompleteTextView) findViewById(R.id.vegie3);

        final AutoCompleteTextView spec =(AutoCompleteTextView) findViewById(R.id.special);
        final AutoCompleteTextView spec2 =(AutoCompleteTextView) findViewById(R.id.special2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getAllVegies());
        vegie1.setAdapter(adapter);
        vegie1.setThreshold(1);
        vegie1.setDropDownHeight(250);
        vegie1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                vegie1.showDropDown();
                return false;
            }
        });

        vegie2.setAdapter(adapter);
        vegie2.setThreshold(1);
        vegie2.setDropDownHeight(250);
        vegie2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                vegie2.showDropDown();
                return false;
            }
        });


        vegie3.setAdapter(adapter);
        vegie3.setThreshold(1);
        vegie3.setDropDownHeight(250);
        vegie3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                vegie3.showDropDown();
                return false;
            }
        });



        ArrayAdapter<String> adapter_spe = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getAllSpecials());
        spec.setAdapter(adapter_spe);
        spec.setThreshold(1);
        spec.setDropDownHeight(250);
        spec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spec.showDropDown();
                return false;
            }
        });

        spec2.setAdapter(adapter_spe);
        spec2.setThreshold(1);
        spec2.setDropDownHeight(250);
        spec2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spec2.showDropDown();
                return false;
            }
        });


        mOneRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOneRadioButton.setChecked(true);
                mTwoRadioButton.setChecked(false);
                mThreeRadioButton.setChecked(false);
                mFourRadioButton.setChecked(false);
                mFiveRadioButton.setChecked(false);
                temp = (EditText) findViewById(R.id.rice);
                temp.setText(mOneRadioButton.getText());

            }
        });
        mTwoRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOneRadioButton.setChecked(false);
                mTwoRadioButton.setChecked(true);
                mThreeRadioButton.setChecked(false);
                mFourRadioButton.setChecked(false);
                mFiveRadioButton.setChecked(false);
                temp = (EditText) findViewById(R.id.rice);
                temp.setText(mTwoRadioButton.getText());

            }
        });
        mThreeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOneRadioButton.setChecked(false);
                mTwoRadioButton.setChecked(false);
                mThreeRadioButton.setChecked(true);
                mFourRadioButton.setChecked(false);
                mFiveRadioButton.setChecked(false);
                temp = (EditText) findViewById(R.id.rice);
                temp.setText(mThreeRadioButton.getText());
            }
        });
        mFourRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOneRadioButton.setChecked(false);
                mTwoRadioButton.setChecked(false);
                mThreeRadioButton.setChecked(false);
                mFourRadioButton.setChecked(true);
                mFiveRadioButton.setChecked(false);
                temp = (EditText) findViewById(R.id.rice);
                temp.setText(mFourRadioButton.getText());
            }
        });

        mFiveRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOneRadioButton.setChecked(false);
                mTwoRadioButton.setChecked(false);
                mThreeRadioButton.setChecked(false);
                mFourRadioButton.setChecked(false);
                mFiveRadioButton.setChecked(true);
                temp = (EditText) findViewById(R.id.rice);
                temp.setText(mFiveRadioButton.getText());
            }
        });


        Button b1 = (Button) findViewById(R.id.uploadbtn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                temp = (EditText) findViewById(R.id.rice);
                toUploadMenuObject.setRice(temp.getText().toString());
                temp = (EditText) findViewById(R.id.roti);
                toUploadMenuObject.setRoti(temp.getText().toString());
                temp = (EditText) findViewById(R.id.vegie1);
                toUploadMenuObject.setVegieOne(temp.getText().toString());
                temp = (EditText) findViewById(R.id.vegie2);
                toUploadMenuObject.setVegieTwo(temp.getText().toString());
                temp = (EditText) findViewById(R.id.vegie3);
                toUploadMenuObject.setVegieThree(temp.getText().toString());
                temp = (EditText) findViewById(R.id.special);
                toUploadMenuObject.setSpecial(temp.getText().toString());
                temp = (EditText) findViewById(R.id.special2);
                toUploadMenuObject.setSpecialExtra(temp.getText().toString());
                temp = (EditText) findViewById(R.id.other);
                toUploadMenuObject.setOther(temp.getText().toString());
                toUploadMenuObject.setMessid(sp.getString("MessId","null"));
                toUploadMenuObject.setDayname(dayname);
                toUploadMenuObject.setMeal(meal);

                AlertDialog.Builder a_b = new AlertDialog.Builder(Upload.this);
                a_b.setMessage("Upload MessMenu ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //new PostMenu().execute();
                                postMenuToServer(toUploadMenuObject);


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

    private void postMenuToServer(MessMenu owner)
    {
        disposable.add(
                apiService
                        .postMenu(owner)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {


                                    Log.e("UploadMenu","Menu Added");



                                Intent intent = new Intent(Upload.this, MainActivity.class);
                                startActivity(intent);


                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(Upload.this,
                                        "Error : " + "Could not add, Try Again"
                                        , Toast.LENGTH_LONG).show();
                                Log.e("Verify Activity", "onError: " + e.getMessage());


                                //startActivity(new Intent(FillDetails.this, VerifyActivity.class));
                                //showError(e);
                            }
                        }));
    }

    private List<String> getAllSpecials() {
        List<String> contactList = new ArrayList<>();
                contactList.add("Aamras");
                contactList.add("Badam Halwa");
                contactList.add("Bhel");
                contactList.add("Boondi Raita");
                contactList.add("Buttermilk");
                contactList.add("Dahi Puri");
                contactList.add("Dahi Vada");
                contactList.add("Dhokla");
                contactList.add("Fafda");
                contactList.add("Fruit Salad");
                contactList.add("Gajar Halwa");
                contactList.add("Ghevar");
                contactList.add("Gulab Jamun");
                contactList.add("Jalebi");
                contactList.add("Kachori");
                contactList.add("Kaju Katli");
                contactList.add("Kheer");
                contactList.add("Lassi");
                contactList.add("Ladoo");
                contactList.add("Modak");
                contactList.add("Mysore Pak");
                contactList.add("Moong Dal Halwa");
                contactList.add("Nariyal Laddu");
                contactList.add("Peda");
                contactList.add("Puran Poli");
                contactList.add("Rabri");
                contactList.add("Shrikhand");
                contactList.add("Sooji Halwa");
                contactList.add("Sheer Khurma");
                contactList.add("Ras Malai");
        return contactList;

    }

    private List<String> getAllVegies() {
        List<String> contactList = new ArrayList<>();
        contactList.add("Aaloo");
        contactList.add("Paneer");
        contactList.add("Chana Masala");
        contactList.add("Cholle");
        contactList.add("Matar");
        contactList.add("Karela");

        return contactList;

    }

}
