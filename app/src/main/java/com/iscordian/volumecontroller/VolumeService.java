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
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("UP")) {
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            } else if (intent.getAction().equals("DOWN")) {
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }
        }
        updateNotification(am);
        return START_STICKY;
    }

    private void updateNotification(AudioManager am) {
        int current = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int percent = (current * 100) / max;

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_layout);
        views.setTextViewText(R.id.volume_text, "Tap buttons to adjust volume (" + percent + "%)");

        Intent up = new Intent(this, VolumeService.class).setAction("UP");
        views.setOnClickPendingIntent(R.id.btn_up, PendingIntent.getService(this, 0, up, PendingIntent.FLAG_UPDATE_CURRENT));

        Intent down = new Intent(this, VolumeService.class).setAction("DOWN");
        views.setOnClickPendingIntent(R.id.btn_down, PendingIntent.getService(this, 1, down, PendingIntent.FLAG_UPDATE_CURRENT));

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off)
                .setContent(views)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .build();

        startForeground(1, notification);
    }

    @Override public IBinder onBind(Intent i) { return null; }
    }
