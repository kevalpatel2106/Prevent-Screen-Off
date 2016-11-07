package com.kevalpatel.preventscreenoff;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by Keval on 07-Nov-16.
 * Class to manage the light intensity.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

class LightIntensityManager {
    //Minimum light intensity required to operate the eye detection.
    private static final float LIGHT_INTENSITY_THRESHOLD = 6F;

    private final AnalyserActivity mAnalyserActivity;

    private final SensorManager mSensorManager;
    private final Sensor mLightSensor;

    private float mLastIntensity = 0f;
    private CountDownTimer mCountDownTimer;

    private final SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            mLastIntensity = event.values[0];

            if (mLastIntensity < LIGHT_INTENSITY_THRESHOLD && mCountDownTimer == null) {
                mCountDownTimer = new CountDownTimer(5000, 5000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if (mLastIntensity < LIGHT_INTENSITY_THRESHOLD)
                            mAnalyserActivity.onLowLightIntensity();

                        mCountDownTimer = null;
                    }
                };
                mCountDownTimer.start();
            }

            Log.d("light sensor", mLastIntensity + "");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    /**
     * Public constructor. This will initialize the light sensor to detect light intensity.
     * To start listening light intensity changes call {@link #startLightMonitoring()}.
     *
     * @param context instance of the caller.
     */
    LightIntensityManager(@NonNull AnalyserActivity context) {
        mAnalyserActivity = context;

        // Obtain references to the SensorManager and the Light Sensor
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


    }

    void startLightMonitoring() {
        mSensorManager.registerListener(listener, mLightSensor, SensorManager.SENSOR_DELAY_UI);
    }

    void stopLightMonitoring() {
        mSensorManager.unregisterListener(listener);

        //stop the timer if running
        if (mCountDownTimer != null) mCountDownTimer.cancel();
    }
}
