package com.outofbox.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.outofbox.model.HourlyUsage;
import com.outofbox.model.UsageEntityView;

public class UsageDrawable extends View {
    private UsageEntityView entityView;
    private Paint hourPaint;
    private Paint usagePaint;
    private Paint textPaint;
    private int width;
    private int height;
    private float[] points;
    private HourlyUsage []hourlyUsage;
    public UsageDrawable(Context context) {
        super(context);
        init();
    }

    public void setHourlyUsage(HourlyUsage []hourlyUsage) {
        this.hourlyUsage = hourlyUsage;
    }

    void init() {
        hourPaint = new Paint();
        hourPaint.setColor(Color.BLACK);
        hourPaint.setStrokeWidth(6f);
        hourPaint.setAntiAlias(true);

        usagePaint = new Paint();
        usagePaint.setColor(Color.RED);
        usagePaint.setStrokeWidth(6f);
        usagePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLUE);
        //textPaint.setStrokeWidth(50F);
        textPaint.setTextSize(20f);
        textPaint.setAntiAlias(true);

        points = new float[48];
    }

    public UsageDrawable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float yCordinate = (float) height * 5 / 6;
        float maxHeight = (float) height * 4 / 6;
        float begin = (float) width * 1 / 6;
        float step = ((float) width * 2 / 3) / 24.0f;
        for (int i = 0; i <= 60; i += 10) {
            canvas.drawText(String.valueOf(i) , width * 1 / 8, yCordinate - maxHeight / 60 * i, textPaint);
            //canvas.drawPoint(width * 1 / 8, yCordinate - maxHeight / 60 * i, usagePaint);
        }
        //canvas.drawLine(width * 1 / 8, yCordinate - 6, width * 1 / 8, yCordinate - maxHeight, usagePaint);

        for (int i = 0; i < points.length; i += 2) {
            points[i] = begin + step * i / 2;
            points[i+1] = yCordinate;
            if (hourlyUsage.length > i && hourlyUsage[i / 2].getDurationOfUsage() != 0) {
                float heightOfUsage = (maxHeight / 60) * hourlyUsage[i / 2].getDurationOfUsage();
                float beginOfY = points[i + 1] - 6;
                canvas.drawLine(points[i], beginOfY, points[i], beginOfY - heightOfUsage, usagePaint);
            }
        }
        canvas.drawPoints(points, hourPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.width = MeasureSpec.getSize(widthMeasureSpec);
        this.height = MeasureSpec.getSize(heightMeasureSpec);
    }
}
