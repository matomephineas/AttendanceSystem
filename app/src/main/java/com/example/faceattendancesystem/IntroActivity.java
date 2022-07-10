package com.example.faceattendancesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.faceattendancesystem.Adapter.IntroViewPagerAdapter;
import com.example.faceattendancesystem.DataHelper.ScreenItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
 private ViewPager viewPager;
 private IntroViewPagerAdapter introViewPagerAdapter;
 TabLayout tabIndicator;
 private Button btnNext,btnGetStarted;
 private int position = 0;
 private Animation btnAnim;
private TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_intro);

        //when this activity is about to launch, we nee to check if its opened before
//        if(restorePrefData())
//        {
//            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }

        btnNext = findViewById(R.id.btn_next);
        btnGetStarted =findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);

        //fill list screen
        final List<ScreenItem> mList =new ArrayList<>();
        boolean fresh_food = mList.add(new ScreenItem("Attendance system","Face Recognition",R.drawable.student));
        mList.add(new ScreenItem("That is so reliable","Attendance system",R.drawable.attendance_word));
        mList.add(new ScreenItem("paper based attendance","To reduce",R.drawable.prsn_attend_icon));
        //setup view pager
        viewPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        viewPager.setAdapter(introViewPagerAdapter);

        //setup tablayout with viewpager

        tabIndicator.setupWithViewPager(viewPager);

        //button get started
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                //savePrefsData();
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position =viewPager.getCurrentItem();
                if(position <mList.size())
                {
                    position++;
                    viewPager.setCurrentItem(position);
                }
                if(position == mList.size())
                {
                   loadLastScreen();
                }

            }
        });
        //tablayout  add change lister
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == mList.size() -1)
                {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mList.size());
            }
        });
    }
    private void savePrefsData()
    {
        SharedPreferences pref= getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor =pref.edit();
        editor.putBoolean("isIntroOpened",true);
        editor.commit();
    }
    private boolean restorePrefData()
    {
        SharedPreferences pref= getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("isIntroOpened",false);
        return isIntroActivityOpenedBefore;

    }
    //show the GETSTARTED Button and hide the indicator and the next button
    private void loadLastScreen()
    {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

        //setup animation
        btnGetStarted.setAnimation(btnAnim);

    }
}