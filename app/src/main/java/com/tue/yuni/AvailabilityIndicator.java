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

        if (availability == 0) {
            Paint p = new Paint();
            p.setColor(Color.GRAY);
            p.setStyle(Paint.Style.FILL);
            p.setTextSize(45);
            canvas.drawText("Out Of Stock", 0, 45, p);
            return;
            /*((GradientDrawable)d.findDrawableByLayerId(R.id.background1)).setColor(Color.GRAY);
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background2)).setColor(Color.GRAY);
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background3)).setColor(Color.GRAY);
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background4)).setColor(Color.GRAY);*/
        }
        else if (availability <= 25) {
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background1)).setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background2)).setColor(Color.LTGRAY);
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background3)).setColor(Color.LTGRAY);
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background4)).setColor(Color.LTGRAY);
         }
        else if (availability <= 50) {
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background1)).setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background2)).setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background3)).setColor(Color.LTGRAY);
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background4)).setColor(Color.LTGRAY);
        }
        else if (availability <= 75) {
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background1)).setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background2)).setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background3)).setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background4)).setColor(Color.LTGRAY);
        }
        else if (availability <= 100) {
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background1)).setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background2)).setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background3)).setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
            ((GradientDrawable)d.findDrawableByLayerId(R.id.background4)).setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
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
