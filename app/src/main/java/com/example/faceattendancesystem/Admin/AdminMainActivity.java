package com.example.faceattendancesystem.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.faceattendancesystem.R;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CourseFragment courseFragment;
    private ModulesFragment modulesFragment;
    private TimeTableFragment timeTableFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

//        toolbar =findViewById(R.id.toolbar2);
//        setSupportActionBar(toolbar);
        viewPager= findViewById(R.id.viewpager2);
        tabLayout =findViewById(R.id.tabs);

        courseFragment=new CourseFragment();
        modulesFragment=new ModulesFragment();
        timeTableFragment=new TimeTableFragment();

        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(courseFragment,"Course");
        viewPagerAdapter.addFragment(modulesFragment,"Modules");
        viewPagerAdapter.addFragment(timeTableFragment,"TimeTable");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.course);
        tabLayout.getTabAt(1).setIcon(R.drawable.list);
        tabLayout.getTabAt(2).setIcon(R.drawable.timetable);

    }


    private class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private List<Fragment> fragments=new ArrayList<>();
        private List<String> fragmentTitles=new ArrayList<>();

        public ViewPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }
        public void addFragment(Fragment fragment,String title)
        {
            fragments.add(fragment);
            fragmentTitles.add(title);

        }
        @NonNull
        @NotNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}