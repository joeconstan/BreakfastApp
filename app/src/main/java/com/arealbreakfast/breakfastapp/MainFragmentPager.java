package com.arealbreakfast.breakfastapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;


public class MainFragmentPager extends FragmentActivity {

    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        MessageBroadcastReceiver receiver = new MessageBroadcastReceiver();
        //registerReceiver(receiver, new IntentFilter("newmessage"));

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver,
                new IntentFilter("newmessage"));

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new LobbyPager(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabss);
        tabLayout.setupWithViewPager(mPager);

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            this.finishAffinity();
            //super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    private class LobbyPager extends FragmentPagerAdapter {

        public LobbyPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence title = "";
            switch (position) {
                case 0:
                    title = "Messages";
                    break;
                case 1:
                    title = "Friends";
                    break;
            }
            return title;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            switch (position) {
                case 0:
                    f = new LobbyFrag();
                    break;
                case 1:
                    f = new FriendsFrag();
                    break;
            }
            return f;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

}

