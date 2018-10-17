package com.outofbox.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

import org.joda.time.DateTime;

import java.util.Date;


@Entity(tableName = "usageEntity")
public class UsageEntity {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "start_time")
    public DateTime startTime;

    @ColumnInfo(name = "end_time")
    public DateTime endTime;

    public UsageEntity(Long id, DateTime startTime, DateTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Ignore
    public UsageEntity(DateTime startTime, DateTime endTime) {
        this(0L, startTime, endTime);
    }

    @Override
    public String toString() {
        return "{Id : " + id + ", StartTime :  " + startTime + ", EndTime :" + endTime + "}";
    }

    public static class DateTimeConvertor {

        @TypeConverter
        public DateTime fromTimeStamp(Long value) {
            return value == null ? null : new DateTime(value);
        }

        @TypeConverter
        public Long dateToTimeStamp(DateTime date) {
            return date == null ? null : date.getMillis();
        }
    }
}
