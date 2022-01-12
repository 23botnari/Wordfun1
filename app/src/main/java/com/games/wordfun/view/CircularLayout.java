package com.games.wordfun.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.games.wordfun.GameActivity;
import com.games.wordfun.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;


public class CircularLayout extends ViewGroup {
    private final double BAD_ANGLE = -1.0;

    private int maxWidth;
    private int maxHeight;

    private final Rect childRect = new Rect();
    private final Point childCenter = new Point();
    private final Point center = new Point();
    private double angleInRadians = BAD_ANGLE;
    private int inflatedChildCount;
    private Paint mPaint;
    private Path mPath;
    private Context mContext;
    private ArrayList<String> word = new ArrayList<>();
    private ArrayList<TextView> chld = new ArrayList<>();
    private StringBuilder s = new StringBuilder();
    public static String generated = "";

    private List<Point> mPoints = new ArrayList<Point>();
    private static final int TOUCH_TOLERANCE_DP = 24;
    private String ch;

    public CircularLayout(Context context) {
        super(context);
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        TypedArray typedArray = context
                .obtainStyledAttributes(attrs, R.styleable.CircularLayoutAttrs);

        int capacity = typedArray.getInteger(R.styleable.CircularLayoutAttrs_capacity, 0);
        if (capacity != 0) {
            setCapacity(capacity);
        }
        /*angle attr always wins*/
        float angle = typedArray.getFloat(R.styleable.CircularLayoutAttrs_angle, (float) BAD_ANGLE);
        if (angle != BAD_ANGLE) {
            setAngle(angle);
        }
        typedArray.recycle();


        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(30);
        mPath = new Path();
    }

    public void setAngle(double degrees) {
        this.angleInRadians = degreesToRadians(degrees);
        requestLayout();
    }

    public void setCapacity(int expectedViewsQuantity) {
        this.angleInRadians = degreesToRadians(360.0 / expectedViewsQuantity);
        requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflatedChildCount = getChildCount();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = max(maxWidth, child.getMeasuredWidth());
                maxHeight = max(maxHeight, child.getMeasuredHeight());
            }
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {

        final int childCount = getChildCount();

        /*if angle hasn't been set, try to calculate it
        taking into account how many items declared
        in xml inside CircularLayout*/
        if (angleInRadians == BAD_ANGLE && inflatedChildCount > 0) {
            setCapacity(childCount);
        }

        if (angleInRadians == BAD_ANGLE) {
            throw new IllegalStateException("set angle or capacity first with " +
                    "setAngle(double degrees)/setCapacity(int expectedViewsQuantity) or " +
                    "with xml angle/capacity attrs.");
        }

        int width = right - left;
        int height = bottom - top;

        if (width != height) {
            throw new IllegalStateException("width should be the same as height");
        }

        int radius = width / 2 - max(maxWidth / 2, maxHeight / 2) - maxPadding();
        center.set(width / 2, width / 2);

        boolean firstIsLaidOut = false;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            chld.add((TextView) child);
            if (child.getVisibility() != View.GONE) {
                if (!firstIsLaidOut) {
                    childCenter.x = center.x;
                    childCenter.y = center.y - radius;
                    firstIsLaidOut = true;
                } else {
                    int deltaX = childCenter.x - center.x;
                    int deltaY = childCenter.y - center.y;
                    double cos = Math.cos(angleInRadians);
                    double sin = Math.sin(angleInRadians);
                    childCenter.x = (int) (center.x + deltaX * cos - deltaY * sin);
                    childCenter.y = (int) (center.y + deltaX * sin + deltaY * cos);
                }

                layoutChild(child);
            }
        }
    }

    private int maxPadding() {
        int paddingTop = getPaddingTop()+9;
        int paddingBottom = getPaddingBottom()+9;
        int paddingLeft = getPaddingLeft()+9;
        int paddingRight = getPaddingRight()+9;
        return max(max(max(paddingTop, paddingBottom), paddingLeft), paddingRight);
    }

    private void layoutChild(View child) {
        int childWidth = child.getMeasuredWidth();
        int childHeight = child.getMeasuredHeight();
        childRect.top = childCenter.y - childHeight / 2;
        childRect.left = childCenter.x - childWidth / 2;
        childRect.right = childRect.left + childWidth;
        childRect.bottom = childRect.top + childHeight;
        child.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);

    }

    private double degreesToRadians(double degrees) {
        return (degrees * Math.PI / 180);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = Math.round(event.getX());
        int y = Math.round(event.getY());

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                mPath.lineTo(event.getX(), event.getY());

//                for (int i = 0; i < getChildCount(); i++) {
//                    TextView child = (TextView) getChildAt(i);
//
//                    if (x > child.getLeft() && x < child.getRight() && y > child.getTop() && y < child.getBottom()) {
//                        float centerX = child.getLeft() + (child.getRight() - child.getLeft()) / 2;
//                        float centerY = child.getTop() + (child.getBottom() - child.getTop()) / 2;
//                        mPath.moveTo(centerX, centerY);
////                            mPath.lineTo(centerX, centerY);
//
////                        Toast.makeText(mContext, child.getText().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }

                break;

            case MotionEvent.ACTION_MOVE:

                Point p = new Point();
                for (int i = 0; i < getChildCount(); i++) {
                    TextView child = (TextView) getChildAt(i);

                    if (x > child.getLeft() && x < child.getRight() && y > child.getTop() && y < child.getBottom()) {
                        float centerX = child.getLeft() + (child.getRight() - child.getLeft()) / 2;
                        float centerY = child.getTop() + (child.getBottom() - child.getTop()) / 2;

                        if (mPoints==null || !mPoints.contains(new Point((int) centerX,(int) centerY))) {
                            mPoints.add(new Point((int) centerX, (int) centerY));
                            ch = child.getText().toString();
                            mPath.lineTo(centerX, centerY);
                            word.add(ch);
                        }

//                        Toast.makeText(mContext, child.getText().toString(), Toast.LENGTH_SHORT).show();
                        p.set((int) centerX, (int) centerY);
                    }
                }

                s = new StringBuilder();
                for (int j = 0; j < word.size(); j++) {
                    s.append(word.get(j));
                }

                GameActivity.wordCatcher(mContext,s.toString());

                invalidate();
                break;

            case MotionEvent.ACTION_UP:

                mPoints.clear();
                s = new StringBuilder();
                for (int j = 0; j < word.size(); j++) {
                    s.append(word.get(j));
                }

                generated = s.toString();
//                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();

                GameActivity.ll_middle.removeAllViews();
                GameActivity.textmatcher(mContext,generated);
                word.clear();
                mPath.reset();
                invalidate();
                break;
        }

        return true;
    }

//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        canvas.drawPath(mPath, mPaint);
//    }
}

