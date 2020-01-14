package com.app.jobfizzerxp.utilities.uiUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.R;

import static android.content.Context.WINDOW_SERVICE;


/**
 * Created by Amanjeet Singh on 03-Feb-17.
 */

public class DoubleArcProgress extends View {
    private RectF oval = new RectF();
    private Paint paint = new Paint();

    int sweepAngle1 = 220;
    int sweepAngle2 = 150;
    int startAngle1 = 120;
    int startAngle2 = 360;

    private float insideRadius;
    private float outsideRadius;
    private int insideArcColor;
    private int outsideArcColor;

    public DoubleArcProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStyle(Paint.Style.STROKE);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DoubleArcProgress,
                0, 0);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }

        //int width = displayMetrics.widthPixels;
        // float xdpi = displayMetrics.xdpi;

        int widthInDP = Math.round(displayMetrics.widthPixels / displayMetrics.density);
       BaseUtils.log("width", "DoubleArcProgress: " + widthInDP);
        int insideInDp = widthInDP / 2;
        int outsideInDp = insideInDp - 30;


        try {
            insideRadius = array.getDimension(R.styleable.DoubleArcProgress_insideArcRadius, insideInDp);
            outsideRadius = array.getDimension(R.styleable.DoubleArcProgress_outsideArcRadius, outsideInDp);
            insideArcColor = array.getColor(R.styleable.DoubleArcProgress_insideArcColor, Color.parseColor("#ffffff"));
            outsideArcColor = array.getColor(R.styleable.DoubleArcProgress_outsideArcColor, Color.parseColor("#ffffff"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            array.recycle();
        }
        post(animate);
        post(animate1);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(outsideArcColor);
        paint.setStrokeWidth(18);
        oval.set(getWidth() / 2 - outsideRadius, getHeight() / 2 - outsideRadius, getWidth() / 2 + outsideRadius, getHeight() / 2 + outsideRadius);
        canvas.drawArc(oval, startAngle1, sweepAngle1, false, paint);

        paint.setColor(insideArcColor);
        paint.setStrokeWidth(12);
        oval.set(getWidth() / 2 - insideRadius, getHeight() / 2 - insideRadius, getWidth() / 2 + insideRadius, getHeight() / 2 + insideRadius);
        canvas.drawArc(oval, startAngle2, sweepAngle2, false, paint);

    }

    private Runnable animate = new Runnable() {
        @Override
        public void run() {
            if (startAngle1 <= 360) {
                startAngle1 += 10;
            } else {
                startAngle1 = 1;
            }

            invalidate();
            postDelayed(this, 27);
        }
    };


    private Runnable animate1 = new Runnable() {
        @Override
        public void run() {

            if (startAngle2 >= 1) {
                startAngle2 -= 10;
            } else {
                startAngle2 = 360;
            }
            invalidate();
            postDelayed(this, 27);
        }
    };


}