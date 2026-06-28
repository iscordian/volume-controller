package com.iscordian.volumecontroller;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.RemoteViews;

public class VolumeService extends Service {
    
    private static long lastClickTime = 0;
    private static final long DOUBLE_CLICK_TIME_DELTA = 350; // Milliseconds timeframe for double-click

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if (action.equals("UP")) {
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            } else if (action.equals("DOWN")) {
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            } else if (action.equals("POWER_CLICK")) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    // Double click confirmed, dispatch execution intent to our running accessibility profile
                    Intent powerMenuIntent = new Intent(this, PowerMenuService.class).setAction("SHOW_POWER_MENU");
                    startService(powerMenuIntent);
                }
                lastClickTime = clickTime;
            }
        }

        showNotification(am);
        return START_STICKY;
    }

    private void showNotification(AudioManager am) {
        int current = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int percent = (max > 0) ? (current * 100) / max : 0;

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_layout);
        views.setTextViewText(R.id.volume_text, "Tap buttons to adjust volume (" + percent + "%)");

        Intent upIntent = new Intent(this, VolumeService.class).setAction("UP");
        PendingIntent pUp = PendingIntent.getService(this, 0, upIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn_up, pUp);

        Intent downIntent = new Intent(this, VolumeService.class).setAction("DOWN");
        PendingIntent pDown = PendingIntent.getService(this, 1, downIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn_down, pDown);

        // Binding for the target Power Layout Component click
        Intent powerIntent = new Intent(this, VolumeService.class).setAction("POWER_CLICK");
        PendingIntent pPower = PendingIntent.getService(this, 2, powerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn_power, pPower);

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off)
                .setContent(views)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX);

        startForeground(1, builder.build());
    }

    @Override public IBinder onBind(Intent i) { return null; }
            }
