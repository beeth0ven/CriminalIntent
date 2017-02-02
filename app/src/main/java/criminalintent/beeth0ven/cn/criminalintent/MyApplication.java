package criminalintent.beeth0ven.cn.criminalintent;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Air on 2017/2/2.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build());
        Log.d("MyApplication", "path: " + Realm.getDefaultInstance().getPath());
    }
}
