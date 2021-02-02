package com.tue.yuni.gui.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.tue.yuni.R;
import com.tue.yuni.models.Availability;

public class TrafficLightIndicator extends View {
    private LayerDrawable d;

    private int red = Color.argb(255, 205, 23, 23);
    private int yellow = Color.YELLOW;
    private int green = Color.argb(255, 12, 183, 18);

    private State state = State.RED;

    public TrafficLightIndicator(Context context) {
        super(context);
    }

    public TrafficLightIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrafficLightIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (d == null) {
            d = (LayerDrawable) getResources().getDrawable(R.drawable.availability_progressbar, null);
        }

        GradientDrawable circle = (GradientDrawable) d.findDrawableByLayerId(R.id.availability);
        circle.setColor(red);

        switch (state) {
            case RED:
                circle.setColor(red);
                break;
            case YELLOW:
                circle.setColor(yellow);
                break;
            case GREEN:
                circle.setColor(green);
                break;
        }

        d.setBounds(0, 0, getWidth(), getHeight());
        d.draw(canvas);
    }

    public void setState(State state) {
        this.state = state;

        invalidate();
    }

    public void setState(Availability availability) {
        switch (availability) {
            case IN_STOCK:
                state = State.GREEN;
                break;
            case LOW_STOCK:
                state = State.YELLOW;
                break;
            case OUT_OF_STOCK:
                state = State.RED;
                break;
        }

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (d == null) {
            d = (LayerDrawable) getResources().getDrawable(R.drawable.availability_progressbar, null);
        }

        setMeasuredDimension(d.getIntrinsicWidth(), d.getIntrinsicHeight());
    }

    public enum State {
        GREEN,
        YELLOW,
        RED
    }
}
