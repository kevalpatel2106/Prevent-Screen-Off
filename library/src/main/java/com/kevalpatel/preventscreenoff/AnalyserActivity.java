package com.kevalpatel.preventscreenoff;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    @Override
    protected void onStart() {
        super.onStart();

        //create fake camera view
        CameraSourcePreview cameraSourcePreview = new CameraSourcePreview(this);
        cameraSourcePreview.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
        cameraSourcePreview.setAlpha(0f);

        View view = ((ViewGroup) getWindow().getDecorView().getRootView()).getChildAt(0);

        Log.d("id", view.getId() + "");
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
//            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            frameLayout.addView(cameraSourcePreview, params);
        } else {
            throw new RuntimeException("Root view of the activity/fragment cannot be frame layout");
        }

        mFaceAnalyser = new FaceAnalyser(this, cameraSourcePreview);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFaceAnalyser.onResumeCalled();

//        startFaceAnalysis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFaceAnalyser.onPauseCalled();
        stopFaceAnalyser();
    }


    public final void startFaceAnalysis() {
        if (mFaceAnalyser == null)
            throw new RuntimeException("Cannot start face analysis in onCreate(). Start it in onStart().");

        mFaceAnalyser.startCameraSource();
    }

    public final void stopFaceAnalyser() {
        mFaceAnalyser.stopFaceTracker();
    }
}
