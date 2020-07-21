package com.example.synerzy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Onboarding extends AppCompatActivity {
    ViewPager viewPager;
    TextView[] dots;
    Button letsGetStartedButton;
    Button skipButton;
    Button nextButton;
    SliderAdapter sliderAdapter;
    LinearLayout dotsLayout;
    int currentpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_onboarding);
        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        letsGetStartedButton=findViewById(R.id.get_started_btn);
        skipButton=findViewById(R.id.skipbtn);

        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        adddots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    public void skip(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();

    }

    public void next(View view){
        viewPager.setCurrentItem(currentpos+1);

    }
    private void adddots(int position) {
        dots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(HtmlCompat.fromHtml("&#8226", HtmlCompat.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            adddots(position);
            currentpos = position;
            if (position == 0) {
                skipButton.setVisibility(View.VISIBLE);
                letsGetStartedButton.setVisibility(View.INVISIBLE);

            } else if (position == 1) {
                letsGetStartedButton.setVisibility(View.INVISIBLE);
                skipButton.setVisibility(View.VISIBLE);
                //nextButton.setVisibility(View.VISIBLE);

            } else {
                letsGetStartedButton.setVisibility(View.VISIBLE);
                skipButton.setVisibility(View.INVISIBLE);
                //nextButton.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}

