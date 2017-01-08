package criminalintent.beeth0ven.cn.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Air on 2016/12/28.
 */

public class CrimeFragment extends Fragment {

    private static final int requestDate = 0;
    private static final int requestTime = 1;

    private Crime crime;
    private EditText textField;
    private Button dateButton;
    private Button timeButton;
    private CheckBox isSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle params = new Bundle();
        params.putSerializable("crimeId", crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(params);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable("crimeId");
        crime = CrimeLab.getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        textField = (EditText) view.findViewById(R.id.textField);
        textField.setText(crime.title);
        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("CrimeFragment","beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("CrimeFragment","onTextChanged");
                crime.title = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("CrimeFragment","afterTextChanged");
            }
        });

        dateButton = (Button) view.findViewById(R.id.dateButton);
        updateDate();
        dateButton.setOnClickListener((buttonView) -> {
            Log.d("CrimeFragment","setOnClickListener");
            FragmentManager fragmentManager = getFragmentManager();
            DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(crime.date);
            datePickerFragment.setTargetFragment(CrimeFragment.this, requestDate);
            datePickerFragment.show(fragmentManager, "DatePickerFragment");
        });

        timeButton = (Button) view.findViewById(R.id.timeButton);
        updateTime();
        timeButton.setOnClickListener((buttonView) -> {
            Log.d("CrimeFragment","setOnClickListener");
            FragmentManager fragmentManager = getFragmentManager();
            TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(crime.date);
            timePickerFragment.setTargetFragment(CrimeFragment.this, requestTime);
            timePickerFragment.show(fragmentManager, "TimePickerFragment");
        });

        isSolvedCheckBox = (CheckBox) view.findViewById(R.id.isSolvedCheckBox);
        isSolvedCheckBox.setChecked(crime.isSolved);
        isSolvedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("CrimeFragment","onCheckedChanged");
            crime.isSolved = isChecked;
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("CrimeFragment","onActivityResult");
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case requestDate:
                Date date = (Date) data.getSerializableExtra("date");
                crime.date = date;
                updateDate();
                break;
            case requestTime:
                Date time = (Date) data.getSerializableExtra("time");
                crime.date = time;
                updateTime();
            default:
                break;
        }
    }

    private void updateDate() {
        Log.d("CrimeFragment","updateDate");
        dateButton.setText(DateFormat.format("EEEE, MMM d, yyyy", crime.date));
    }

    private void updateTime() {
        Log.d("CrimeFragment","updateTime");
        timeButton.setText(DateFormat.format("h:mm a", crime.date));
    }
}



