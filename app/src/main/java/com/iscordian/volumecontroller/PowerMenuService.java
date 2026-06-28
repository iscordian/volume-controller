package com.iscordian.volumecontroller;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

public class PowerMenuService extends AccessibilityService {
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "SHOW_POWER_MENU".equals(intent.getAction())) {
            // Android 5.1 native constant to reveal global system power-down options dialog box
            performGlobalAction(GLOBAL_ACTION_POWER_DIALOG);
        }
        return START_STICKY;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Required callback but no implementation needed for this use case
    }

    @Override
    public void onInterrupt() {
        // Required callback but no implementation needed for this use case
    }
}
