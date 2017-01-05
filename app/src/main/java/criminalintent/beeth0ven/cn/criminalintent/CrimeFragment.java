package criminalintent.beeth0ven.cn.criminalintent;

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

import java.util.UUID;

/**
 * Created by Air on 2016/12/28.
 */

public class CrimeFragment extends Fragment {

    private Crime crime;
    private EditText textField;
    private Button dateButton;
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
        dateButton.setText(crime.date.toString());
        dateButton.setText(DateFormat.format("EEEE, MMM d, yyyy", crime.date));
        dateButton.setOnClickListener((buttonView) -> {
            Log.d("CrimeFragment","setOnClickListener");
            FragmentManager fragmentManager = getFragmentManager();
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.show(fragmentManager, "DatePickerFragment");
        });

        isSolvedCheckBox = (CheckBox) view.findViewById(R.id.isSolvedCheckBox);
        isSolvedCheckBox.setChecked(crime.isSolved);
        isSolvedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("CrimeFragment","onCheckedChanged");
            crime.isSolved = isChecked;
        });

        return view;
    }
}



