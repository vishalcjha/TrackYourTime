package com.outofbox.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.outofbox.model.UsageEntity;
import com.outofbox.model.UsageEntityView;
import com.outofbox.repository.UsageEntityRepository;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class UsageEntityViewModel extends AndroidViewModel {

    public LiveData<List<UsageEntityView>> usageEntitiesByDay;
    private UsageEntityRepository usageEntityRepository;
    public UsageEntityViewModel(@NonNull Application application) {
        super(application);
        usageEntityRepository = new UsageEntityRepository(application);
        usageEntitiesByDay = Transformations.map(usageEntityRepository.getAllEntities(), new Function<List<UsageEntity>, List<UsageEntityView>>() {
            @Override
            public List<UsageEntityView> apply(List<UsageEntity> input) {
                return UsageEntityViewModel.getUsageEntitiesByDay(input);
            }
        });
    }

    private static List<UsageEntityView> getUsageEntitiesByDay(List<UsageEntity> usageEntities) {
        List<UsageEntityView> usageEntityViews = new ArrayList<>();
        List<Pair<DateTime, DateTime>> usageInADay = null;
        UsageEntity lastUsage = null;
        for (UsageEntity usageEntity: usageEntities) {
            if (lastUsage == null) {
                lastUsage = usageEntity;
                usageInADay = new ArrayList<>();
                usageInADay.add(new Pair(lastUsage.startTime, lastUsage.endTime));
            } else {
                if (usageEntity.startTime.getDayOfMonth() != lastUsage.startTime.getDayOfMonth()) {
                    usageEntityViews.add(new UsageEntityView.UsageEntityViewBuilder(lastUsage.startTime, usageInADay).build());
                    usageInADay = new ArrayList<>();
                    usageInADay.add(new Pair(usageEntity.startTime, usageEntity.endTime));
                } else {
                    //TODO - fix boundary when date range cross particular date
                    if (usageEntity.startTime.getDayOfMonth() != usageEntity.endTime.getDayOfMonth()) {
                        usageInADay.add(new Pair(usageEntity.startTime, usageEntity.endTime));
                    } else {
                        usageInADay.add(new Pair(usageEntity.startTime, usageEntity.endTime));
                    }
                }
                lastUsage = usageEntity;
            }
        }

        if (usageInADay != null) {
            usageEntityViews.add(new UsageEntityView.UsageEntityViewBuilder(usageInADay.get(0).first, usageInADay).build());
        }
        return usageEntityViews;
    }

    public void addSampleData(List<UsageEntity> sampleData) {
        for(UsageEntity usageEntity: sampleData) {
            usageEntityRepository.insertEntity(usageEntity);
        }
    }

    public void deleteUsage() {
        usageEntityRepository.deleteAllEntity();
    }

    public void insertEntity(UsageEntity usageEntity) {
        usageEntityRepository.insertEntity(usageEntity);
    }
}
