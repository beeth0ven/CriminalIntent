package criminalintent.beeth0ven.cn.criminalintent;

import android.os.Environment;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Air on 2016/12/28.
 */

public class Crime extends RealmObject {
    @PrimaryKey
    public long id;
    public String title;
    public Date date;
    public boolean isSolved;
    public String suspect;
    public String phoneNumber;

    public Crime() {
        id = new Date().getTime();
        date = new Date();
    }

    public String getImageFilename() {
        return "IMG_" + String.valueOf(id) + ".jpg";
    }

    public File getImageFile() {
        File dir = MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (dir == null) { return null; }
        return new File(dir, getImageFilename());
    }

}
