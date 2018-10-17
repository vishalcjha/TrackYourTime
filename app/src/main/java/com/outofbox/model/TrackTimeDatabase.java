package com.outofbox.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {UsageEntity.class}, version = 1, exportSchema = false)
@TypeConverters({UsageEntity.DateTimeConvertor.class})
public abstract class TrackTimeDatabase extends RoomDatabase {
    private static final String TRACK_TIME = "track_time";
    private static volatile TrackTimeDatabase INSTANCE;
    public abstract UsageEntityDao usageEntityDao();

    public static TrackTimeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TrackTimeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TrackTimeDatabase.class, TRACK_TIME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
