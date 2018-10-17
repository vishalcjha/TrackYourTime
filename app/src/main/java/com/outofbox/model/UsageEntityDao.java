package com.outofbox.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UsageEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsageEntity(UsageEntity ue);

    @Query("SELECT * FROM usageEntity ORDER BY start_time DESC")
    public LiveData<List<UsageEntity>> getAllUsageEntity();


    @Query("SELECT * FROM usageEntity WHERE start_time >= :startTime AND end_time <= :endTime ORDER BY start_time DESC")
    public LiveData<List<UsageEntity>> getUsageEntityByRange(long startTime, long endTime);

    @Query("DELETE FROM usageEntity")
    public void deleteAllUsage();

    @Query("DELETE FROM usageEntity where start_time >= :startTime AND end_time <= :endTime")
    public void deleteUsageEntityByRange(long startTime, long endTime);
}
