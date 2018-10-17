package com.outofbox.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.outofbox.model.UsageEntity;
import com.outofbox.repository.UsageEntityRepository;
import com.outofbox.viewModel.UsageEntityViewModel;

import org.joda.time.DateTime;

public class TrackerService extends Service {
    private static final String TAG = TrackerService.class.getSimpleName();
    public TrackerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //No one can bind to this service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        BroadcastReceiver screenAction = new ScreenAction(new DateTime(), null);
        registerReceiver(screenAction, ScreenAction.getIntentFilter());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        //BroadcastReceiver screenAction = new ScreenAction();
        //registerReceiver(screenAction, ScreenAction.getIntentFilter());
        //stopSelf();
        return START_NOT_STICKY;
    }

    public static class ScreenAction extends BroadcastReceiver {
        private DateTime lastActionTime;
        private UsageEntityViewModel usageEntityViewModel;

        public ScreenAction(DateTime lastActionTime, UsageEntityViewModel usageEntityViewModel) {
            this.lastActionTime = lastActionTime;
            this.usageEntityViewModel = usageEntityViewModel;
        }

        //private static final String TAG = ScreenAction.class.getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean screenOff = false;
            if (lastActionTime != null) {
                Log.d(TAG, "onReceive: Last Action Time " + lastActionTime);
            }
            Log.d(TAG, "OnReceive of ScreenAction");
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_OFF:
                    usageEntityViewModel.insertEntity(new UsageEntity(lastActionTime, new DateTime()));
                    lastActionTime = null;
                    screenOff = true;
                    Log.d(TAG, "onReceive: " + intent.getAction());
                    break;
                case Intent.ACTION_SCREEN_ON:
                    lastActionTime = new DateTime();
                    screenOff = false;
                    Log.d(TAG, "onReceive: " + intent.getAction());
                    break;
                case Intent.ACTION_USER_PRESENT:
                    screenOff = false;
                    Log.d(TAG, "onReceive: " + intent.getAction());
                    break;
                 default:
                    Log.d(TAG, "onReceive: " + intent.getAction());
                    break;
            }
            /*Intent trackerIntent = new Intent(context, TrackerService.class);
            trackerIntent.putExtra("SCREEN_OFF", screenOff);
            context.startService(trackerIntent);*/
        }

        public static IntentFilter getIntentFilter() {
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            return intentFilter;
        }
    }

    public static class BootCompleted extends BroadcastReceiver {
        //private static final String TAG = BootCompleted.class.getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                //IntentFilter intentFilter = ScreenAction.getIntentFilter();
                //LocalBroadcastManager.getInstance(context).registerReceiver(new ScreenAction(), intentFilter);
                Intent serviceIntent = new Intent(context, TrackerService.class);
                context.startService(serviceIntent);
            }
        }
    }
}
