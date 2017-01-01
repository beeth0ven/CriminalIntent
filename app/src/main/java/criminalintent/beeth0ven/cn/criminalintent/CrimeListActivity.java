package criminalintent.beeth0ven.cn.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Air on 2017/1/1.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
