package criminalintent.beeth0ven.cn.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Air on 2017/1/5.
 */

public class DatePickerFragment extends DialogFragment {

    private DatePicker datePicker;

    public static DatePickerFragment newInstance(Date date) {
        Bundle params = new Bundle();
        params.putSerializable("date", date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(params);
        return datePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        Date date = (Date) getArguments().getSerializable("date");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        datePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Log.d("DatePickerFragment","setPositiveButton");
                    int year1 = datePicker.getYear();
                    int month1 = datePicker.getMonth();
                    int day1 = datePicker.getDayOfMonth();
                    Date date1 = new GregorianCalendar(year1, month1, day1).getTime();
                    sendResult(Activity.RESULT_OK, date1);
                })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("date", date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
