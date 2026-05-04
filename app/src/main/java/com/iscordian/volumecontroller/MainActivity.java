package com.iscordian.volumecontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Corrected: Just use startService(intent)
        Intent intent = new Intent(this, VolumeService.class);
        startService(intent);
        
        Toast.makeText(this, "Volume Controller Activated", Toast.LENGTH_SHORT).show();
        
        finish();
    }
}
