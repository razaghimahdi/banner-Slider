package com.razzaghi.mysliderimage;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View parent_view;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();

    private static String[] array_image_place = {
            "https://dkstatics-public.digikala.com/digikala-adservice-banners/e23df5bcf003f8d2f7e1e1ab2170df878a0ba3eb_1594410854.jpg?x-oss-process=image/quality,q_80",
            "https://dkstatics-public.digikala.com/digikala-adservice-banners/e23df5bcf003f8d2f7e1e1ab2170df878a0ba3eb_1594410854.jpg?x-oss-process=image/quality,q_80",
            "https://i.pinimg.com/236x/15/b1/e7/15b1e7af0c64dcde90f4375a1df9a80b.jpg",
            "https://i.pinimg.com/236x/15/b1/e7/15b1e7af0c64dcde90f4375a1df9a80b.jpg",
    };

    private static String[] array_title_place = {
            "test",
            "test",
            "test",
            "test",
    };

    private static String[] array_brief_place = {
            "test",
            "test",
            "test",
            "test",
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initToolbar();
        initComponent();
    }
/*
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Places");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       setSystemBarColor(this);
    }*/

    private void initComponent() {
        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(this, new ArrayList<Image2>());

        final List<Image2> items = new ArrayList<>();
        for (int i = 0; i < array_image_place.length; i++) {
            Image2 obj = new Image2();
            obj.image = array_image_place[i];
            //obj.imageDrw = getResources().getDrawable(Integer.parseInt(obj.image));
            obj.name = array_title_place[i];
            obj.brief = array_brief_place[i];
            items.add(obj);


            Log.i("TAG", "initComponent: "+obj.image);
            Log.i("TAG", "initComponent: "+obj.name);
            Log.i("TAG", "initComponent: "+obj.brief);


        }

        adapterImageSlider.setItems(items);
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        ((TextView) findViewById(R.id.title)).setText(items.get(0).name);
        ((TextView) findViewById(R.id.brief)).setText(items.get(0).brief);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                ((TextView) findViewById(R.id.title)).setText(items.get(pos).name);
                ((TextView) findViewById(R.id.brief)).setText(items.get(pos).brief);
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        startAutoSlider(adapterImageSlider.getCount());
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
        }
    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }


    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        //private List<Image> items;
        private List<Image2> items2;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, Image2 obj);
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<Image2> items) {
            this.act = activity;
            this.items2 = items;
        }

        @Override
        public int getCount() {
            return this.items2.size();
        }

        public void setItems(List<Image2> items) {
            this.items2 = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //final Image o = items.get(position);
            final Image2 o2 = items2.get(position);
            Log.i("TAG", "instantiateItem: "+o2);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView image =  v.findViewById(R.id.image);
            MaterialRippleLayout lyt_parent =  v.findViewById(R.id.lyt_parent);
            //displayImageOriginal(act, image, o.image);
            displayImageFromUrl(act, image, o2.image);
            Log.i("TAG", "instantiateItem: "+o2.image);

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }


    public static void displayImageOriginal(Context ctx, ImageView img, @DrawableRes int drawable) {
        try {
            Glide.with(ctx).load(drawable)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
        } catch (Exception e) {
        }
    }

    public static void displayImageFromUrl(Context ctx, ImageView img, String drawable) {
        try {
            Glide.with(ctx).load(drawable)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
            Log.i("TAG", "displayImageFromUrl: "+drawable);
        } catch (Exception e) {
            Log.e("TAG", "displayImageFromUrl: "+e.toString());
        }
    }


}