package com.example.newonlinechatapp2022.PagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.newonlinechatapp2022.Fragments.CallFragment;
import com.example.newonlinechatapp2022.Fragments.ChatFragment;
import com.example.newonlinechatapp2022.Fragments.StatusFragment;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    int tabCount;
    public ViewPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount = behavior;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new CallFragment();
            case 1:
                return new ChatFragment();
            case 2:
                return new StatusFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
