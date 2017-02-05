package criminalintent.beeth0ven.cn.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Air on 2017/1/1.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView crimeRecyclerView;
    private LinearLayout emptyView;
    private Button newCirmeButton;
    private CrimeAdapter adapter;
    private int selectedPosition;
    private boolean isSubtitleShow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        crimeRecyclerView = (RecyclerView) view.findViewById(R.id.crimeRecyclerView);
        crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            isSubtitleShow = savedInstanceState.getBoolean("isSubtitleShow");
        }

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);
        newCirmeButton = (Button) view.findViewById(R.id.newCirmeButton);
        newCirmeButton.setOnClickListener((buttonView) -> addCrime());

        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem showSubtitleMenuItem = menu.findItem(R.id.showSubtitleMenuItem);
        int title = isSubtitleShow ? R.string.hide_subtitle : R.string.show_subtitle;
        showSubtitleMenuItem.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newCrimeMenuItem:
                addCrime();
                return true;
            case R.id.showSubtitleMenuItem:
                isSubtitleShow = !isSubtitleShow;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addCrime() {
        Crime crime = new Crime();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(crime);
        realm.commitTransaction();
        updateUI();
        showCrime(crime);
    }

    private void updateSubtitle() {
        int crimeCount = CrimeLab.crimes.size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

        if (!isSubtitleShow) { subtitle = null; }

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void showEmptyViewIfNeeded() {
        emptyView.setVisibility(CrimeLab.crimes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isSubtitleShow", isSubtitleShow);
    }

    public void updateUI() {
        if (adapter == null) {
            RealmResults<Crime> crimes = CrimeLab.crimes;
            adapter = new CrimeAdapter(crimes);
            crimeRecyclerView.setAdapter(adapter);
            crimes.addChangeListener((updatedCrimes) -> {
                adapter.notifyDataSetChanged();
                showEmptyViewIfNeeded();
            });
            updateSubtitle();
            showEmptyViewIfNeeded();
        }
    }

    private void showCrime(Crime crime) {
        if (getActivity().findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.id);
            getActivity().startActivity(intent);
        } else {
            Fragment crimeFragment = CrimeFragment.newInstance(crime.id);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, crimeFragment)
                    .commit();
        }
    }


    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTextView;
        private TextView dateTextView;
        private CheckBox checkBox;

        private Crime crime;
        public int position;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }

        public void bindCrime(Crime crime) {
            this.crime = crime;
            titleTextView.setText(crime.title);
            dateTextView.setText(DateFormat.format("EEEE, MMM d, yyyy", crime.date));
            checkBox.setChecked(crime.isSolved);
        }

        @Override
        public void onClick(View v) {
            showCrime(crime);
            selectedPosition = position;
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private RealmResults<Crime> crimes;

        public CrimeAdapter(RealmResults<Crime> crimes) {
            this.crimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            holder.bindCrime(crimes.get(position));
            holder.position = position;
        }

        @Override
        public int getItemCount() {
            return crimes.size();
        }
    }
}
