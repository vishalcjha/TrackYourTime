package com.outofbox.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.outofbox.model.TrackTimeDatabase;
import com.outofbox.model.UsageEntity;
import com.outofbox.model.UsageEntityDao;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsageEntityRepository {

    private UsageEntityDao usageEntityDao;
    private LiveData<List<UsageEntity>> usageEntities;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final String TAG = UsageEntityRepository.class.getSimpleName();

    public UsageEntityRepository(Application application) {
        TrackTimeDatabase db = TrackTimeDatabase.getDatabase(application);
        usageEntityDao = db.usageEntityDao();
        usageEntities = usageEntityDao.getAllUsageEntity();
    }

    public void insertEntity(final UsageEntity usageEntity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: Adding new usage" + usageEntity);
                usageEntityDao.insertUsageEntity(usageEntity);
            }
        });
    }

    public void deleteEntity(UsageEntity usageEntity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void deleteAllEntity() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                usageEntityDao.deleteAllUsage();
            }
        });
    }

    public void deleteRange(final long startTime, final long endTime) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                usageEntityDao.deleteUsageEntityByRange(startTime, endTime);
            }
        });
    }

    public LiveData<List<UsageEntity>> getEntitiesByRange(long startTime, int endTime) {
        return usageEntityDao.getUsageEntityByRange(startTime, endTime);
    }

    public LiveData<List<UsageEntity>> getAllEntities() {
        return usageEntityDao.getAllUsageEntity();
    }
}
