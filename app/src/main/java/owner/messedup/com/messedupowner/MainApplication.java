package owner.messedup.com.messedupowner;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


import owner.messedup.com.messedupowner.data.remote.APIManager;

public class MainApplication extends Application {
    public static APIManager apiManager;
    //private BoxStore boxStore;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        setTheme(R.style.AppTheme);

        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);
        apiManager = APIManager.getInstance();
        //boxStore = MyObjectBox.builder().androidContext(MainApplication.this).build();

        /*if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(boxStore).start(this);
        }
        Log.d("MainApplication", "Using ObjectBox " + BoxStore.getVersion() + " (" + BoxStore.getVersionNative() + ")");*/
    }

    public static RefWatcher getRefWatcher(Context context) {
        MainApplication application = (MainApplication) context.getApplicationContext();
        return application.refWatcher;
    }
    /*public BoxStore getBoxStore() {
        return boxStore;
    }*/
}
