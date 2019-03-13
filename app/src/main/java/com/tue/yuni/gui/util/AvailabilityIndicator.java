package com.tue.yuni.gui.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tue.yuni.R;

public class AvailabilityIndicator extends View {
    private LayerDrawable d;
    private int availability = 0;
    private int[] threshold = new int[]{25, 50, 100};
    private int[] color = new int[]{Color.RED, Color.YELLOW, Color.GREEN};

    public AvailabilityIndicator(Context context) {
        super(context);
    }

    public AvailabilityIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AvailabilityIndicator, 0, 0);
        threshold = new int[3];
        color = new int[3];
        try {
            threshold[0] = a.getInt(R.styleable.AvailabilityIndicator_lowThreshold, 25);
            threshold[1] = a.getInt(R.styleable.AvailabilityIndicator_mediumThreshold, 50);
            threshold[2] = a.getInt(R.styleable.AvailabilityIndicator_highThreshold, 100);
            color[0] = a.getColor(R.styleable.AvailabilityIndicator_lowThresholdColor, Color.argb(255, 205, 23, 23));
            color[1] = a.getColor(R.styleable.AvailabilityIndicator_mediumThresholdColor, Color.YELLOW);
            color[2] = a.getColor(R.styleable.AvailabilityIndicator_highThresholdColor, Color.argb(255, 12, 183, 18));
        } finally {
            a.recycle();
        }
    }

    public AvailabilityIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AvailabilityIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (d == null)
            d = (LayerDrawable)getResources().getDrawable(R.drawable.availability_progressbar, null);
        GradientDrawable circle = (GradientDrawable)d.findDrawableByLayerId(R.id.availability);

        int i = 0;
        circle.setColor(color[0]);

        if (0 <= availability && availability <= threshold[0]) {
            circle.setColor(color[0]);
        } else if (threshold[0] < availability && availability <= threshold[1]) {
            circle.setColor(color[1]);
        } else if (threshold[1] < availability && availability <= threshold[2]) {
            circle.setColor(color[2]);
        }

        d.setBounds(0, 0, getWidth(), getHeight());
        d.draw(canvas);
    }

    public void setAvailability(int availability){
        this.availability = availability;
        invalidate();
    }

    public int getAvailability() {
        return availability;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (d == null)
            d = (LayerDrawable)getResources().getDrawable(R.drawable.availability_progressbar, null);
        setMeasuredDimension(d.getIntrinsicWidth(), d.getIntrinsicHeight());
    }

    public void setThreshold(int index, int threshold) {
        this.threshold[index] = threshold;
    }

    public void setColor(int index, int color) {
        this.color[index] = color;
    }

    public int getThreshold(int index) {
        return threshold[index];
    }

    public int getColor(int index) {
        return color[index];
    }
}
