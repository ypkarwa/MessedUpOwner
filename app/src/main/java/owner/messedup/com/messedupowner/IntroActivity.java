package owner.messedup.com.messedupowner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static owner.messedup.com.messedupowner.Const.OWNER_PREFS;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);



        findViewById(R.id.btnIntroRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences(OWNER_PREFS, MODE_PRIVATE)
                        .edit()
                        .putInt("isStartPoint", 1)
                        .apply();
                finish();
                startActivity(new Intent(IntroActivity.this, PhoneNumberAuthentication.class));
            }
        });
    }
}
