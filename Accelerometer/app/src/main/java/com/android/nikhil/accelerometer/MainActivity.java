/* Created by Nikhil PV */

package com.android.nikhil.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView x, y, z;
    private Sensor accelerometer;
    private SensorManager sensorManager;
    private Button saveBtn;
    private long mLastClickTime = 0;   // variable to track event time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x = (TextView) findViewById(R.id.x);
        y = (TextView) findViewById(R.id.y);
        z = (TextView) findViewById(R.id.z);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        saveBtn = (Button) findViewById(R.id.save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Preventing multiple clicks, using threshold of 3 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                printLogs();
                Toast.makeText(MainActivity.this, "Log saved", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        x.setText("X = " + event.values[0]);
        y.setText("Y = " + event.values[1]);
        z.setText("Z = " + event.values[2]);

        Log.e("Accelerometer project ", "X value " + event.values[0] + " Y value " + event.values[1] + " Z value "
                + event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void printLogs() {
        File filename = new File(Environment.getExternalStorageDirectory() + "/accelerometer.txt");
        try {
            if (!filename.exists())
                filename.createNewFile();
            String cmd = "logcat -d -f" + filename.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
