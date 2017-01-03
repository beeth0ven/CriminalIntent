package criminalintent.beeth0ven.cn.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Air on 2017/1/1.
 */

public class CrimeActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context pacageContext, UUID id) {
        Intent intent = new Intent(pacageContext, CrimeActivity.class);
        intent.putExtra("id", id);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
