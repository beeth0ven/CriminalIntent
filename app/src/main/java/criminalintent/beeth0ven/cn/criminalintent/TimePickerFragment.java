package criminalintent.beeth0ven.cn.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Air on 2017/1/8.
 */

public class TimePickerFragment extends DialogFragment {

    private TimePicker timePicker;

    static public TimePickerFragment newInstance(Date time) {
        Bundle params = new Bundle();
        params.putSerializable("time", time);

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(params);
        return timePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        Date time = (Date) getArguments().getSerializable("time");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    int hour1 = timePicker.getCurrentHour();
                    int minute1 = timePicker.getCurrentMinute();
                    Date time1 = new GregorianCalendar(year, month, day, hour1, minute1).getTime();
                    sendResult(Activity.RESULT_OK, time1);
                })
                .create();
    }

    private void sendResult(int resultCode, Date time) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("time", time);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
