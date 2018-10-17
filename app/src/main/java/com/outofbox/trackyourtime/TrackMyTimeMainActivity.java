package com.outofbox.trackyourtime;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.outofbox.model.UsageEntityView;
import com.outofbox.trackyourtime.databinding.ActivityTrackMyTimeMainBinding;
import com.outofbox.ui.DailyUsageEntityAdapter;
import com.outofbox.utility.SampleUsageEntityData;
import com.outofbox.viewModel.UsageEntityViewModel;

import java.util.ArrayList;
import java.util.List;

public class TrackMyTimeMainActivity extends AppCompatActivity {

    private final static String TAG = TrackMyTimeMainActivity.class.getSimpleName();
    private List<UsageEntityView> usageEntitiesByDay = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActivityTrackMyTimeMainBinding binding;
    private UsageEntityViewModel viewModel;
    private DailyUsageEntityAdapter dailyUsageEntityAdapter;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_track_my_time_main);
        recyclerView = binding.activityCard;
        toolBar = binding.mainToolbar;
        setSupportActionBar(toolBar);

        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getApplicationContext(),
                DividerItemDecoration.HORIZONTAL));

    }

    private void initViewModel() {
        final Observer<List<UsageEntityView>> observer = new Observer<List<UsageEntityView>>() {
            @Override
            public void onChanged(@Nullable List<UsageEntityView> newUsageEntities) {
                usageEntitiesByDay.clear();
                usageEntitiesByDay.addAll(newUsageEntities);

                if (dailyUsageEntityAdapter == null) {
                    dailyUsageEntityAdapter = new DailyUsageEntityAdapter(usageEntitiesByDay, TrackMyTimeMainActivity.this);
                    recyclerView.setAdapter(dailyUsageEntityAdapter);
                    //recyclerView.addItemDecoration(new UsageEntitiyDecoration(getApplicationContext()));
                } else {
                    dailyUsageEntityAdapter.notifyDataSetChanged();
                };
            }
        };
        viewModel = ViewModelProviders.of(this).get(UsageEntityViewModel.class);
        viewModel.usageEntitiesByDay.observe(this, observer);
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

    private void deleteSampleData() {
        viewModel.deleteUsage();
    }

    private void addSampleData() {
        viewModel.addSampleData(SampleUsageEntityData.getPreFilledSampleEntities(7));
    }
}
