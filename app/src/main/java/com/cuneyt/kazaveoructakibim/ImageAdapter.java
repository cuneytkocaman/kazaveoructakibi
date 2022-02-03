package com.cuneyt.kazaveoructakibim;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.cuneyt.kazaveoructakibim.R;

public class ImageAdapter extends PagerAdapter {

    private Context mContext;
    private int[] imageId = new int[]{R.drawable.img_info1, R.drawable.img_info2, R.drawable.img_info3, R.drawable.img_info4};

    ImageAdapter(Context context){
        mContext = context;
    }
    @Override
    public int getCount() {
        return imageId.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(imageId[position]);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

}
