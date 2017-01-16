package criminalintent.beeth0ven.cn.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Date;

/**
 * Created by Air on 2017/1/8.
 */

public class DatePickerActivity extends SingleFragmentActivity {

    public static Intent newInstence(Context pacageContext, Date date) {
        Intent intent = new Intent(pacageContext, DatePickerActivity.class);
        intent.putExtra("date", date);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Date date = (Date) getIntent().getSerializableExtra("date");
        return DatePickerFragment.newInstance(date);
    }
}
