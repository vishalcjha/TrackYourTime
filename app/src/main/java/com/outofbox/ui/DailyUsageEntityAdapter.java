package com.outofbox.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.outofbox.model.HourlyUsage;
import com.outofbox.model.UsageEntityView;
import com.outofbox.trackyourtime.R;
import com.outofbox.trackyourtime.databinding.DailyUsageEntityElementBinding;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.List;

public class DailyUsageEntityAdapter extends RecyclerView.Adapter<DailyUsageEntityAdapter.ViewHolder> {

    private List<UsageEntityView> usageEntitiesByDay;
    private Context context;

    public DailyUsageEntityAdapter(List<UsageEntityView> usageEntitiesByDay, Context context) {
        this.usageEntitiesByDay = usageEntitiesByDay;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        DailyUsageEntityElementBinding viewHolder = DataBindingUtil.inflate(layoutInflater, R.layout.daily_usage_entity_element, viewGroup, false);
        return new ViewHolder(viewHolder, context, viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        DataPoint dataPoints[] = new DataPoint[24];
        UsageDrawable usageDrawable = viewHolder.binding.usageGraph;
        usageDrawable.setHourlyUsage(usageEntitiesByDay.get(pos).getHourlyUsage());
    }

    @Override
    public int getItemCount() {
        return usageEntitiesByDay.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private DailyUsageEntityElementBinding binding;
        private Context context;
        private ViewGroup parent;
        public ViewHolder(final DailyUsageEntityElementBinding binding, Context context, ViewGroup parent) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
            this.parent = parent;
        }

        ViewGroup getParent() {
            return parent;
        }
    }
}
