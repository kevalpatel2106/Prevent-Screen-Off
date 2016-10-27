package com.kevalpatel.preventscreenoff;

/**
 * Created by Keval on 27-Oct-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public interface FaceTrackerListener {

    void onUserAttentionGone();

    void onUserAttentionAvailable();

    void onScreenOffPrevented();

    void onCameraPermissionNotAvailable();

    void onErrorOccurred();
}
