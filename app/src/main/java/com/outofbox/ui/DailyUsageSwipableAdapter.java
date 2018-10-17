package com.outofbox.ui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.outofbox.model.UsageEntityView;

import org.joda.time.DateTime;

import java.util.List;

public class DailyUsageSwipableAdapter extends FragmentStatePagerAdapter {
    private List<UsageEntityView> usageEntityViews;
    public DailyUsageSwipableAdapter(FragmentManager fm, List<UsageEntityView> usageEntityViews) {
        super(fm);
        this.usageEntityViews = usageEntityViews;
    }

    @Override
    public Fragment getItem(int i) {
        DailyUsageSwipableFragment swipableFragment = new DailyUsageSwipableFragment();
        swipableFragment.init(usageEntityViews.get(i));
        return swipableFragment;
    }

    @Override
    public int getCount() {
        return usageEntityViews.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (usageEntityViews.size() == 0) {
            return "";
        }
        DateTime date = usageEntityViews.get(position).getDateOfUsage();
        String pageDate = date.getMonthOfYear() + "-" + date.getDayOfMonth() + "-" + date.getYear();
        return pageDate;
    }
}
