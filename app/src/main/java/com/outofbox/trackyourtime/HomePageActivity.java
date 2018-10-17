package com.outofbox.trackyourtime;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.outofbox.model.UsageEntityView;
import com.outofbox.service.TrackerService;
import com.outofbox.ui.DailyUsageSwipableAdapter;
import com.outofbox.utility.SampleUsageEntityData;
import com.outofbox.viewModel.UsageEntityViewModel;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private UsageEntityViewModel viewModel;
    private Toolbar toolbar;
    private TextView emptyDataText;
    private PagerTitleStrip pagerTitleStrip;
    private List<UsageEntityView> usageEntitiesByDay = new ArrayList<>();

    private TrackerService trackerService;
    private BroadcastReceiver screenOnOffReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        emptyDataText = findViewById(R.id.empty_data_text);
        viewPager = findViewById(R.id.daily_usage_swipable_element);
        pagerTitleStrip = findViewById(R.id.pager_title_strip);
        initViewModel();
        initService();
        registerBroadCastReceiver();
    }

    private void registerBroadCastReceiver() {
        screenOnOffReceiver = new TrackerService.ScreenAction(new DateTime(), viewModel);
        registerReceiver(screenOnOffReceiver, TrackerService.ScreenAction.getIntentFilter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadCastReceiver();
    }

    private void unregisterBroadCastReceiver() {
        unregisterReceiver(screenOnOffReceiver);
    }

    private void initService() {
        /*Intent trackerServiceIntent = new Intent(this.getApplicationContext(), TrackerService.class);
        this.getApplicationContext().startService(trackerServiceIntent);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.add_sample_data) :
                addSampleData();
                break;
            case (R.id.clear_sample_data) :
                deleteSampleData();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    void initViewModel() {
        final Observer<List<UsageEntityView>> observer = new Observer<List<UsageEntityView>>() {
            @Override
            public void onChanged(@Nullable List<UsageEntityView> usageEntityViews) {
                usageEntitiesByDay.clear();
                usageEntitiesByDay.addAll(usageEntityViews);
                if (usageEntitiesByDay.size() == 0) {
                    emptyDataText.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.GONE);
                    pagerTitleStrip.setVisibility(View.GONE);
                } else {
                    emptyDataText.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    pagerTitleStrip.setVisibility(View.VISIBLE);
                }

                if (pagerAdapter == null) {
                    pagerAdapter = new DailyUsageSwipableAdapter(getSupportFragmentManager(), usageEntitiesByDay);
                    viewPager.setAdapter(pagerAdapter);
                } else {
                    pagerAdapter.notifyDataSetChanged();
                }
            }
        };

        viewModel = ViewModelProviders.of(this).get(UsageEntityViewModel.class);
        viewModel.usageEntitiesByDay.observe(this, observer);
    }

    private void deleteSampleData() {
        viewModel.deleteUsage();
    }

    private void addSampleData() {
        viewModel.addSampleData(SampleUsageEntityData.getPreFilledSampleEntities(7));
    }

}
