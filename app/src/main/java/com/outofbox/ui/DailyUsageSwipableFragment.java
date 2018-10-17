package com.outofbox.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.outofbox.model.UsageEntity;
import com.outofbox.model.UsageEntityView;
import com.outofbox.trackyourtime.R;
import com.outofbox.trackyourtime.databinding.ActivityHomePageBinding;
import com.outofbox.trackyourtime.databinding.ActivityTrackMyTimeMainBinding;
import com.outofbox.trackyourtime.databinding.FragmentDailyUsageSwipableBinding;

public class DailyUsageSwipableFragment extends Fragment {

    private UsageEntityView usageEntity;

    public void init(UsageEntityView usageEntity) {
        this.usageEntity = usageEntity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDailyUsageSwipableBinding dataBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_daily_usage_swipable, container, false);
        if (usageEntity != null) {
            dataBinding.usageGraph.setHourlyUsage(usageEntity.getHourlyUsage());
        }
        return dataBinding.getRoot();
    }
}
