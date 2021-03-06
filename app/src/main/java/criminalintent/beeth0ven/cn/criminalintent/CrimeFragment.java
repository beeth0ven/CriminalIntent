package criminalintent.beeth0ven.cn.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by Air on 2016/12/28.
 */

public class CrimeFragment extends Fragment {

    private static final int requestDate = 0;
    private static final int requestTime = 1;
    private static final int requestContact = 2;
    private static final int requestImage = 3;

    private Crime crime;
    private EditText textField;
    private Button dateButton;
    private Button timeButton;
    private CheckBox isSolvedCheckBox;
    private Button reportButton;
    private Button suspectButton;
    private Button makeCallButton;
    private ImageButton imageButton;
    private ImageButton imageThumbnailButton;

    public static CrimeFragment newInstance(long crimeId) {
        Bundle params = new Bundle();
        params.putSerializable("crimeId", crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(params);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long crimeId = (long) getArguments().getSerializable("crimeId");
        crime = CrimeLab.getCrime(crimeId);
        setHasOptionsMenu(true);
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
                Realm.getDefaultInstance().executeTransaction(realm -> crime.title = s.toString());
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
            Intent intent = DatePickerActivity.newInstence(getActivity(), crime.date);
            startActivityForResult(intent, requestDate);
        });

        timeButton = (Button) view.findViewById(R.id.timeButton);
        updateTime();
        timeButton.setOnClickListener((buttonView) -> {
            Log.d("CrimeFragment","setOnClickListener");
            TimePickerFragment.showFrom(CrimeFragment.this, requestTime, crime.date);
        });

        isSolvedCheckBox = (CheckBox) view.findViewById(R.id.isSolvedCheckBox);
        isSolvedCheckBox.setChecked(crime.isSolved);
        isSolvedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("CrimeFragment","onCheckedChanged");
            Realm.getDefaultInstance().executeTransaction(realm -> crime.isSolved = isChecked);
        });

        reportButton = (Button) view.findViewById(R.id.reportButton);
        reportButton.setOnClickListener(v -> {
            Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .setText(getCirmeReport())
                    .setSubject(getString(R.string.crime_report_subject))
                    .getIntent();
            intent = Intent.createChooser(intent, getString(R.string.send_report));
            startActivity(intent);
        });

        final Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        suspectButton = (Button) view.findViewById(R.id.suspectButton);
        suspectButton.setOnClickListener(v -> startActivityForResult(contactIntent, requestContact));

        if (crime.suspect != null) { suspectButton.setText(crime.suspect); }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(contactIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            suspectButton.setEnabled(false);
        }

        makeCallButton = (Button) view.findViewById(R.id.makeCallButton);
        makeCallButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + crime.phoneNumber));
            startActivity(intent);
        });

        makeCallButton.setEnabled(crime.phoneNumber != null);

        imageButton = (ImageButton) view.findViewById(R.id.imageButton);
        final Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (crime.getImageFile() != null && imageIntent.resolveActivity(packageManager) != null) {
            Uri uri = Uri.fromFile(crime.getImageFile());
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        imageButton.setOnClickListener(v -> startActivityForResult(imageIntent, requestImage));

        imageThumbnailButton = (ImageButton) view.findViewById(R.id.imageThumbnailButton);
        imageThumbnailButton.setOnClickListener(a -> {
            Intent intent = ImageActivity.newInstanse(getActivity(), crime.getImageFile().getPath());
            startActivity(intent);
        });

        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Log.d("CrimeFragment","didLayoutSubviews");
            updateImageView();
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteCrimeMenuItem:
                CrimeLab.deleteCrimeWithId(crime.id);
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
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
                Realm.getDefaultInstance().executeTransaction(realm -> crime.date = date);
                updateDate();
                break;
            case requestTime:
                Date time = (Date) data.getSerializableExtra("time");
                Realm.getDefaultInstance().executeTransaction(realm -> crime.date = time);
                updateTime();
            case requestContact:
                if (data == null) { break; }
                String[] queryFields = new String[] {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };

                Cursor cursor = getActivity().getContentResolver()
                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, queryFields, null, null, null);

                try {
                    if (cursor.getCount() == 0) { return; }
                    cursor.moveToFirst();
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                                crime.suspect = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                crime.phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            });
                    suspectButton.setText(crime.suspect);
                    makeCallButton.setEnabled(crime.phoneNumber != null);
                } finally {
                    cursor.close();
                }
            case requestImage:
                updateImageView();
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

    private void updateImageView() {
        if (crime.getImageFile() == null || !crime.getImageFile().exists()) {
            imageThumbnailButton.setImageDrawable(null);
            imageThumbnailButton.setEnabled(false);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    crime.getImageFile().getPath(),
                    imageThumbnailButton.getWidth(),
                    imageThumbnailButton.getHeight()
            );
            imageThumbnailButton.setImageBitmap(bitmap);
            imageThumbnailButton.setEnabled(true);
        }
    }

    private String getCirmeReport() {
        String solvedString = getString(crime.isSolved ? R.string.crime_report_solved : R.string.crime_report_unsolved);
        String dateString = DateFormat.format("EEE, MM dd", crime.date).toString();
        String suspect = crime.suspect == null ?
                getString(R.string.crime_report_no_suspect) :
                getString(R.string.crime_report_suspect, crime.suspect);
        return getString(R.string.crime_report, crime.title, dateString, solvedString, suspect);
    }
}



