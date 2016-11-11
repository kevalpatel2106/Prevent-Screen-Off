/*
 * Copyright 2016 Keval Patel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kevalpatel.preventscreenoff;

import android.content.Context;
import android.os.PowerManager;
import android.support.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Keval on 07-Nov-16.
 * This class will manage wakelocks.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

class WakelockManager {
    private static final String WAKE_LOCK_TAG = "FaceTacker Wakelock";      //Tag for the wake lock

    private PowerManager.WakeLock mWakeLock;    //Wakelock to keep screen on


    /**
     * Initialization.
     *
     * @param analyserActivity {@link AnalyserActivity} instance
     */
    WakelockManager(@NonNull AnalyserActivity analyserActivity) {
        PowerManager powerManager = (PowerManager) analyserActivity.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, WAKE_LOCK_TAG);
    }

    /**
     * Acquire the wakelock.
     */
    void acquireWakelock() {
        if (!mWakeLock.isHeld()) mWakeLock.acquire();
    }

    /**
     * Release the wakelock.
     */
    void releaseWakelock() {
        //Wait for the 5 seconds, so that screen does not turn off automatically.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mWakeLock.isHeld()) mWakeLock.release();
            }
        }, 5000);
    }
}
