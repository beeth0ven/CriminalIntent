package criminalintent.beeth0ven.cn.criminalintent;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Air on 2017/2/4.
 */

public class ImageFragment extends DialogFragment {

    private ImageView imageView;

    public static ImageFragment newInstanse(String imagePath) {
        Bundle arguments = new Bundle();
        arguments.putString("imagePath", imagePath);
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.setArguments(arguments);
        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image, container, false);
        String imagePath = getArguments().getString("imagePath");
        if (imagePath == null) { return view; }
        Bitmap bitmap = PictureUtils.getScaledBitmap(imagePath, getActivity());
        imageView.setImageBitmap(bitmap);
        return view;
    }
}

