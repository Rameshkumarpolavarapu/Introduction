package com.example.ramesh.introduction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
public class WelcomeActivty extends AppCompatActivity {
    private ViewPager viewPager;
    private MyViewPageAdapter myViewPageAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip,btnNext;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Checking for first time launch - before calling setContentView()
        prefManager     =   new PrefManager(getApplicationContext());
        if (!prefManager.isFirstTimeLaunch()){
            launchHomeScreen();
            finish();
        }
        //making notification bar Transparent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_welcome_activty);

        viewPager   = (ViewPager) findViewById(R.id.viewpager);
        dotsLayout  = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip     = (Button) findViewById(R.id.btn_Skip);
        btnNext     = (Button) findViewById(R.id.btn_Next);

        //layouts of all welcome sliders
        layouts     =   new int[]{
                    R.layout.welcome_side1,
                    R.layout.welcome_side2,
                    R.layout.welcome_slide3,
                    R.layout.welcome_slide4
        };
        //adding bottom dots
        addBottomDots(0);
        // making notification bar transparent
        changeStatusBarColor();
        myViewPageAdapter   =   new MyViewPageAdapter();
        viewPager.setAdapter(myViewPageAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangedListener);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchHomeScreen();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check for last page
                //if last page home screen vl be launched
                int current =   getItem(+1);
                if (current <   layouts.length){
                    //move to next screen
                    viewPager.setCurrentItem(current);
                }else {
                    launchHomeScreen();
                }
            }
        });
    }
    private void changeStatusBarColor() {
        Window window   =   getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLUE);
        }
    }
    private void addBottomDots(int currentPage) {
        dots    =   new TextView[layouts.length];
        int[] colorsActive  =   getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive    =   getResources().getIntArray(R.array.array_dot_inactive);
        dotsLayout.removeAllViews();
        for (int i =  0;i<dots.length;i++){
            dots[i]     =   new TextView(getApplicationContext());

            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length>0){
            dots[currentPage].setTextColor(colorsActive[currentPage  ]);
        }
    }
    private int getItem(int i){
        return viewPager.getCurrentItem()+i;
    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(getApplicationContext(),NavigationDrawer.class));
        finish();
    }
    ViewPager.OnPageChangeListener viewPagerPageChangedListener =   new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            //changing the next button text 'NEXT'  /   'Got it'
            if (position    ==  layouts.length-1){
                    //last page make button text to Got it
                    btnNext.setText(getText(R.string.start));
                    btnSkip.setVisibility(View.GONE);
            }else {
                //still page are left

                btnNext.setText(getText(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }
        @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { } @Override public void onPageScrollStateChanged(int state) { }
    };
    /**
     * View Page Adapter
     */
    private class MyViewPageAdapter extends PagerAdapter {
       private LayoutInflater layoutInflater;
        public MyViewPageAdapter(){ } @Override public Object instantiateItem(ViewGroup container, int position) { layoutInflater  = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); View view =   layoutInflater.inflate(layouts[position],container,false); container.addView(view); return view; } @Override public int getCount() { return layouts.length;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==  object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view   = (View) object;
            container.removeView(view);
        }
    }
}
