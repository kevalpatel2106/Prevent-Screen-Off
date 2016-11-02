package com.kevalpatel.preventscreenoff;

/**
 * Created by Keval on 27-Oct-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public interface ScreenListner {

    void onScreenMonitoringStart();

    void onScreenMonitoringStop();

    void onErrorOccurred(int errorCode);
}
