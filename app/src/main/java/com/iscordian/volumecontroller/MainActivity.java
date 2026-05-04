package com.iscordian.volumecontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Start the volume service
        Intent intent = new Intent(this, VolumeService.class);
        startContext.startService(intent);
        
        Toast.makeText(this, "Volume Controller Activated", Toast.LENGTH_SHORT).show();
        
        // Close the activity immediately to keep it clean
        finish();
    }
}
