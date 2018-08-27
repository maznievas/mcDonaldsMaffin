package com.example.andrey.mcdonaldsmaffin.edit_activity;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpActivity;
import com.example.andrey.mcdonaldsmaffin.R;
import com.example.andrey.mcdonaldsmaffin.util.listeners.OnPinchListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by andrey on 28.08.18.
 */


public class EditActivity extends MvpActivity implements EditView, View.OnTouchListener{

    // these matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

// we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

// remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;

    @BindView(R.id.staticImage)
    ImageView imageView;

    @BindView(R.id.parentLayout)
    ViewGroup layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_edit);

        ButterKnife.bind(this);

        init();
    }

    public void init()
    {

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // handle touch events here

        ImageView view = (ImageView) v;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                savedMatrix.set(matrix);

                start.set(event.getX(), event.getY());

                mode = DRAG;

                lastEvent = null;

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];

                lastEvent[0] = event.getX(0);

                lastEvent[1] = event.getX(1);

                lastEvent[2] = event.getY(0);

                lastEvent[3] = event.getY(1);

               // d = rotation(event);

                break;

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_POINTER_UP:

                mode = NONE;

                lastEvent = null;

                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {

                    matrix.set(savedMatrix);

                    float dx = event.getX() - start.x;

                    float dy = event.getY() - start.y;

                    matrix.postTranslate(dx, dy);

                } else if (mode == ZOOM) {

                    float newDist = spacing(event);

                    if (newDist > 10f) {

                        matrix.set(savedMatrix);

                        float scale = (newDist / oldDist);

                        matrix.postScale(scale, scale, mid.x, mid.y);

                    }
                    if (lastEvent != null && event.getPointerCount() == 3) {
                        Log.d("mLog", "maybe rotation");

                      //  newRot = rotation(event);

                        float r = newRot - d;

                        float[] values = new float[9];

                        matrix.getValues(values);

                        float tx = values[2];

                        float ty = values[5];

                        float sx = values[0];

                        float xc = (view.getWidth() / 2) * sx;

                        float yc = (view.getHeight() / 2) * sx;

                       // matrix.postRotate(r, tx + xc, ty + yc);

                    }

                }

                break;
        }

        view.setImageMatrix(matrix);

        return true;
    }

    /**
     102.
     * Determine the space between the first two fingers
     103.
     */

    private float spacing(MotionEvent event) {

        float x = event.getX(0) - event.getX(1);

        float y = event.getY(0) - event.getY(1);

        return (float)Math.sqrt(x * x + y * y);

    }



/**
 111.
 * Calculate the mid point of the first two fingers
 112.
 */

    private void midPoint(PointF point, MotionEvent event) {

        float x = event.getX(0) + event.getX(1);

        float y = event.getY(0) + event.getY(1);

        point.set(x / 2, y / 2);

    }
}