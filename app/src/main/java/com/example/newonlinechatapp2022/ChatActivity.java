package com.example.newonlinechatapp2022;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.newonlinechatapp2022.PagerAdapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ChatActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem tabCall,tabStatus,tabChat;
    Toolbar toolbar;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = (Toolbar)findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);



        tabLayout = findViewById(R.id.tabLayout);
        tabStatus = (TabItem)findViewById(R.id.tabStatus);
        tabCall = (TabItem)findViewById(R.id.tabCalls);
        tabChat = (TabItem)findViewById(R.id.tabChat);
        viewPager = (ViewPager)findViewById(R.id.fragmentContainer);


        adapter = new ViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0 || tab.getPosition() == 1 || tab.getPosition() == 2){
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }
}