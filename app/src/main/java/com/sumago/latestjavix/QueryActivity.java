package com.sumago.latestjavix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class QueryActivity extends AppCompatActivity {
    static int QI=1;
    static String VAL="";
    static String VALI="";
    static ArrayList<String> arrayListStatic=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        viewPager = (ViewPager) findViewById(R.id.pager);
        addTabs(viewPager);



        //tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(viewPager);
        //setupTabIcons();


    }
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.searchpng,
            R.drawable.searchpng,
            R.drawable.searchpng,
            R.drawable.searchpng
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(QueryActivity.QI==1){}
        else QueryActivity.QI=QueryActivity.QI-1;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ListQuery(QueryActivity.arrayListStatic), "APPLE");
        adapter.addFrag(new ListQuery(QueryActivity.arrayListStatic), "Orange");
        viewPager.setAdapter(adapter);
    }
    // Adapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    // End Adapter

}