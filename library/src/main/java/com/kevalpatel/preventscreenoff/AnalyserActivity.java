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

public abstract class AnalyserActivity extends AppCompatActivity implements FaceTrackerListener {
    private FaceAnalyser mFaceAnalyser;

    private boolean isForcedStop = false;

    @Override
    protected void onStart() {
        super.onStart();
        mFaceAnalyser = new FaceAnalyser(this, addPreView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFaceAnalyser.onResumeCalled();

        if (!isForcedStop && !mFaceAnalyser.isTrackingRunning()) startFaceAnalysis();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mFaceAnalyser.isTrackingRunning()) mFaceAnalyser.stopFaceTracker();
    }


    public final void startFaceAnalysis() {
        if (mFaceAnalyser == null)
            throw new RuntimeException("Cannot start face analysis in onCreate(). Start it in onStart().");

        mFaceAnalyser.startFaceTracker();
    }


    public final void stopFaceAnalyser() {
        isForcedStop = true;
        mFaceAnalyser.stopFaceTracker();
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
