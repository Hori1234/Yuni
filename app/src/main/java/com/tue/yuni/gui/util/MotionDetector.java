package com.tue.yuni.gui.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.SENSOR_SERVICE;

public class MotionDetector implements SensorEventListener {
    // Shake Detection Settings
    private static final float SHAKE_GRAVITY = 2.5f;
    private static final int SHAKE_SLOP_TIME_MS = 250;
    private static final int SHAKE_RESET_TIME_MS   = 3000;
    // Sensors
    private final SensorManager _sensorManager;
    private final Sensor _accelerometer;
    // Motions
    private boolean shakeDetectionEnable = false;
    // Shake Variables
    private int shakeCount = 0;
    private long lastShakeTime = 0;
    public OnShakeListener shakeListener;

    // Constuctor
    public MotionDetector(Context ctx){
        _sensorManager = (SensorManager)ctx.getSystemService(SENSOR_SERVICE);
        _accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        registerListener();
    }

    // Enable / Disable Shake Detection
    public void setShakeDetection(boolean enable) {
        shakeDetectionEnable = enable;
    }

    // Register used sensors listeners
    private void registerListener(){
        _sensorManager.registerListener(this, _accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Unregister all sensors listeners
    private void unregisterListener(){
        _sensorManager.unregisterListener(this);
    }

    // Sensor Event
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float gForces[] = new float[]{
                    event.values[0] / SensorManager.GRAVITY_EARTH,
                    event.values[1] / SensorManager.GRAVITY_EARTH,
                    event.values[2] / SensorManager.GRAVITY_EARTH
            };
            float gForce = (float)Math.sqrt(gForces[0] * gForces[0] + gForces[1] * gForces[1] + gForces[2] * gForces[2]);

            if (shakeDetectionEnable) {
                if (gForce > SHAKE_GRAVITY) {
                    long time = System.currentTimeMillis();

                    if (lastShakeTime + SHAKE_SLOP_TIME_MS > time) return;
                    if (lastShakeTime + SHAKE_RESET_TIME_MS < time) shakeCount = 0;

                    lastShakeTime = time;
                    shakeCount++;

                    shakeListener.onShake(shakeCount);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not Required
    }
}
