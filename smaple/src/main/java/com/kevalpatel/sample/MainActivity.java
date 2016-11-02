package com.kevalpatel.sample;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.kevalpatel.preventscreenoff.AnalyserActivity;
import com.kevalpatel.preventscreenoff.Errors;
import com.kevalpatel.preventscreenoff.ScreenListner;

public class MainActivity extends AnalyserActivity      //Inherit AnalyseActivity to automatically manage activity callback.
        implements ScreenListner {                      //Implement the listener to get the callbacks

    private static final int RC_HANDLE_CAMERA_PERM = 123;
    private static final int NOTIFICATION_ID = 109;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onScreenMonitoringStart() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_eye)
                .setContentTitle("Prevent Screen Off")
                .setAutoCancel(false)
                .setContentText("We are currently monitoring the user eyes.");

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onScreenMonitoringStop() {
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(NOTIFICATION_ID);
    }

    @Override
    public void onErrorOccurred(int errorCode) {
        switch (errorCode) {
            case Errors.UNDEFINED:
                Snackbar.make(findViewById(R.id.activity_main), "Error occurred.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                break;
            case Errors.CAMERA_PERMISSION_NOT_AVAILABLE:
                requestCameraPermission();
                break;
            case Errors.FRONT_CAMERA_NOT_AVAILABLE:
                Snackbar.make(findViewById(R.id.activity_main), "Front camera is not available.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                break;
            case Errors.LOW_LIGHT:
                Snackbar.make(findViewById(R.id.activity_main), "Not enough light is available.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                break;
            case Errors.PLAY_SERVICE_NOT_AVAILABLE:
                Snackbar.make(findViewById(R.id.activity_main), "Play services is not installed.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startEyeTracking();
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(findViewById(R.id.activity_main), R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, listener)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startEyeTracking();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Prevent Screen off")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .show();
    }
}
