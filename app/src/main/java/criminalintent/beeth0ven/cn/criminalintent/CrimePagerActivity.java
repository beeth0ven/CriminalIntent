package criminalintent.beeth0ven.cn.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by Air on 2017/1/4.
 */

public class CrimePagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Crime> crimes;

    public static Intent newIntent(Context pacageContext, long crimeId) {
        Intent intent = new Intent(pacageContext, CrimePagerActivity.class);
        intent.putExtra("crimeId", crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        crimes = CrimeLab.crimes;
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = crimes.get(position);
                return CrimeFragment.newInstance(crime.id);
            }

            @Override
            public int getCount() {
                return crimes.size();
            }
        });

        long crimeId = (long) getIntent().getSerializableExtra("crimeId");
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).id == crimeId) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
