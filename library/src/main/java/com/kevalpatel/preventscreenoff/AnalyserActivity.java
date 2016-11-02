package com.kevalpatel.preventscreenoff;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Keval on 27-Oct-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public abstract class AnalyserActivity extends AppCompatActivity implements ScreenListner {
    private FaceAnalyser mFaceAnalyser;
    private boolean isForcedStop = false;

    @Override
    protected void onStart() {
        super.onStart();

        //initialize the face analysis
        mFaceAnalyser = new FaceAnalyser(this, addPreView());
    }

    @Override
    protected void onResume() {
        super.onResume();

        //start face tracking when application is foreground
        if (!isForcedStop && !mFaceAnalyser.isTrackingRunning()) startEyeTracking();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //stop face tracking when application goes to background
        if (mFaceAnalyser.isTrackingRunning()) stopEyeTrackingInternal();
    }

    /**
     * Start the eye tracking and front camera.
     */
    public final void startEyeTracking() {
        if (mFaceAnalyser == null)
            throw new RuntimeException("Cannot start eye analysis in onCreate(). Start it in onStart().");

        isForcedStop = false;
        if (!mFaceAnalyser.isTrackingRunning()) mFaceAnalyser.startEyeTracker();
    }

    /**
     * Stop face analysis and release front camera.
     */
    public final void stopEyeTracking() {
        isForcedStop = true;
        stopEyeTrackingInternal();
    }

    /**
     * Stop face analysis and release front camera.
     */
    private void stopEyeTrackingInternal() {
        if (mFaceAnalyser.isTrackingRunning()) mFaceAnalyser.stopEyeTracker();
    }

    /**
     * Add camera preview to the root of the activity layout.
     *
     * @return {@link CameraSourcePreview}
     */
    private CameraSourcePreview addPreView() {
        //create fake camera view
        CameraSourcePreview cameraSourcePreview = new CameraSourcePreview(this);
        cameraSourcePreview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View view = ((ViewGroup) getWindow().getDecorView().getRootView()).getChildAt(0);

        if (view instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) view;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1, 1);
            linearLayout.addView(cameraSourcePreview, params);
        } else if (view instanceof RelativeLayout) {
            RelativeLayout relativeLayout = (RelativeLayout) view;

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1, 1);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            relativeLayout.addView(cameraSourcePreview, params);
        } else if (view instanceof FrameLayout) {
            FrameLayout frameLayout = (FrameLayout) view;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(1, 1);
            frameLayout.addView(cameraSourcePreview, params);
        } else {
            throw new RuntimeException("Root view of the activity/fragment cannot be frame layout");
        }

        return cameraSourcePreview;
    }
}
