package com.tue.yuni;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

public class AvailabilityIndicator extends View {
    private int availability = 0;

    public AvailabilityIndicator(Context context) {
        super(context);
    }

    public AvailabilityIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        LayerDrawable d = (LayerDrawable)getResources().getDrawable(R.drawable.availability_progressbar, null);
        GradientDrawable circle = (GradientDrawable)d.findDrawableByLayerId(R.id.availability);

        if (availability <= 25) {
            circle.setColor(Color.argb(255, 205, 23, 23));
         }
        else if (availability <= 50) {
            circle.setColor(Color.YELLOW);
        }
        else if (availability <= 100) {
            circle.setColor(Color.argb(255, 12, 183, 18));
        }

        d.setBounds(0, 0, getWidth(), getHeight());
        d.draw(canvas);
    }

    public void setAvailability(int availability){
        this.availability = availability;
    }

    public int getAvailability() {
        return availability;
    }
}
