package com.example.gachabro.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.gachabro.R;

import android.os.Handler;
import android.os.Looper;

public class BannerPager_adapter extends PagerAdapter {

    private final Context context;
    private final int[] images;
    private final ViewPager viewPager;
    private final Handler handler;

    public BannerPager_adapter(Context context, int[] images, ViewPager viewPager, Handler handler) {
        this.context = context;
        this.images = images;
        this.viewPager = viewPager;
        this.handler = new Handler(Looper.getMainLooper());
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.banner_slide, container, false);

        ImageView imageView = layout.findViewById(R.id.banner_pic);
        imageView.setImageResource(images[position]);

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void startAutoScroll() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                viewPager.setCurrentItem((currentItem + 1) % viewPager.getAdapter().getCount(), true);
                handler.postDelayed(this, 3000); // Change picture every 3 seconds
            }
        };
        handler.post(runnable);
    }

    public void stopAutoScroll() {
        handler.removeCallbacksAndMessages(null);
    }
}
