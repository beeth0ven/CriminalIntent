package criminalintent.beeth0ven.cn.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Air on 2017/1/5.
 */

public class DatePickerFragment extends DialogFragment {

    private DatePicker datePicker;
    private Button okButton;

    public static DatePickerFragment newInstance(Date date) {
        Bundle params = new Bundle();
        params.putSerializable("date", date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(params);
        return datePickerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.dialog_date, container, false);

        Date date = (Date) getArguments().getSerializable("date");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        datePicker.init(year, month, day, null);

        okButton = (Button) view.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            Log.d("DatePickerFragment","setOnClickListener");
            int year1 = datePicker.getYear();
            int month1 = datePicker.getMonth();
            int day1 = datePicker.getDayOfMonth();
            Date date1 = new GregorianCalendar(year1, month1, day1).getTime();
            sendResult(Activity.RESULT_OK, date1);
        });

        return view;
    }

    private void sendResult(int resultCode, Date date) {

        Intent intent = new Intent();
        intent.putExtra("date", date);

        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        } else {
            getActivity().setResult(resultCode, intent);
        }
    }
}
