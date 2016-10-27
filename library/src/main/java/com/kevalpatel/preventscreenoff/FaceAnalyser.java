package com.kevalpatel.preventscreenoff;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

/**
 * Created by Keval on 27-Oct-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

class FaceAnalyser {
    private static final int RC_HANDLE_GMS = 4525;

    private static final String TAG = "face";
    private FaceDetector mDetector;
    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private PowerManager.WakeLock mWakeLock;

    private FaceTrackerListener mScreenListener;
    private Activity mActivity;

    /**
     * Public constructor.
     *
     * @param activity activity.
     */
    FaceAnalyser(AnalyserActivity activity, CameraSourcePreview preview) {
        if (activity != null) {
            mActivity = activity;
            mScreenListener = activity;
        } else {
            throw new RuntimeException("Cannot start without callback listener.");
        }

        if (preview != null) {
            mPreview = preview;
        } else {
            throw new RuntimeException("Cannot start without camera source preview.");
        }

        final PowerManager pm = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "FaceTacker Wakelock");
    }

    void onResumeCalled() {
        long screenOnTiming = android.provider.Settings.System.getLong(mActivity.getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT, 0);
    }

    void onPauseCalled() {
    }

    void stopFaceTracker() {
        if (mDetector != null) mDetector.release();
        if (mPreview != null)  mPreview.release();
    }


    private void creteCameraTracker() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mScreenListener.onCameraPermissionNotAvailable();
            return;
        }

        mDetector = new FaceDetector.Builder(mActivity)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        mDetector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        if (!mDetector.isOperational()) mScreenListener.onErrorOccurred();

        mCameraSource = new CameraSource.Builder(mActivity, mDetector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
    }


    void startCameraSource() {
        creteCameraTracker();

        // check that the device has play services available.
        int statusCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                mActivity.getApplicationContext());
        if (statusCode != ConnectionResult.SUCCESS) {
            Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(mActivity, statusCode, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    /**
     * Factory for creating a face tracker to be associated with a new face.  The multiprocessor
     * uses this factory to create face trackers as needed -- one for each individual.
     */
    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker();
        }
    }

    /**
     * Face tracker for each detected individual. This maintains a face graphic within the app's
     * associated face overlay.
     */
    private class GraphicFaceTracker extends Tracker<Face> {

        private GraphicFaceTracker() {
        }

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        @Override
        public void onNewItem(int faceId, Face item) {
            Log.d(TAG, "onNewItem" + faceId);
        }

        /**
         * Update the position/characteristics of the face within the overlay.
         */
        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            Log.d(TAG, "onUpdate" + face.getIsLeftEyeOpenProbability());

            if (face.getIsLeftEyeOpenProbability() > 0.10 && face.getIsRightEyeOpenProbability() > 0.10) {
                if (!mWakeLock.isHeld()) mWakeLock.acquire();
                mScreenListener.onUserAttentionAvailable();
            } else {
                if (mWakeLock.isHeld()) mWakeLock.release();
                mScreenListener.onUserAttentionGone();
            }
        }

        /**
         * Hide the graphic when the corresponding face was not detected.  This can happen for
         * intermediate frames temporarily (e.g., if the face was momentarily blocked from
         * view).
         */
        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            Log.d(TAG, "onMissing");
        }

        /**
         * Called when the face is assumed to be gone for good. Remove the graphic annotation from
         * the overlay.
         */
        @Override
        public void onDone() {
            if (mWakeLock.isHeld()) mWakeLock.release();
            mScreenListener.onUserAttentionGone();
        }
    }
}
