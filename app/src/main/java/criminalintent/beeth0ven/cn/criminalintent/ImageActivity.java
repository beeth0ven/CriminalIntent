package criminalintent.beeth0ven.cn.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Air on 2017/2/4.
 */

public class ImageActivity extends SingleFragmentActivity {

    public static Intent newInstanse(Context context, String imagePath) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("imagePath", imagePath);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String imagePath = getIntent().getStringExtra("imagePath");
        return ImageFragment.newInstanse(imagePath);
    }
}
