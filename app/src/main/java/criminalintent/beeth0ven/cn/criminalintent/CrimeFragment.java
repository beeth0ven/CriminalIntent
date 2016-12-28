package criminalintent.beeth0ven.cn.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Air on 2016/12/28.
 */

public class CrimeFragment extends Fragment {

    private Crime crime;
    private EditText textField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crime = new Crime();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        textField = (EditText) view.findViewById(R.id.textField);
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
        return view;
    }
}



