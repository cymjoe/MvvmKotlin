package com.cymjoe.lib_widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;


public class HomeAnimView extends View {
    Context mContext;
    private PointF mCenter;
    private int mWidth;
    private int mHeight;
    private int mPercent = 100;
    //两个圆环开始颜色 结束颜色
    int startArcColor, endArcColor;
    int waveColor = 0xaaA194FF;
    //中间旋转进度条颜色
    int startProgressColor, endProgressColor;
    //旋转角度
    int rotate;
    boolean showProgress = false;
    //基础角度
    private int startAngel = -90;

    /**
     * 绘制波浪的画笔
     */
    private Paint progressPaint;
    RectF circleRectF;
    int xOffset = 0;
    private int curProgress = 100;
    int sweepAngle = 230;
    float unitAngle;
    int startAngle = 0;
    //绘制文字的画笔
    private Paint textPaint;

    //绘制水波的路径
    private Path wavePath;
    //每一个像素对应的弧度数
    private float RADIANS_PER_X;
    //去除边框后的半径（即内圆半径）
    private int VALID_RADIUS;
    //波纹振幅与半径之比。(建议设置：<0.1)
    private static final float A = 0.09f;
    /**
     * 进度条最大值和当前进度值
     */
    private float max = 100f, progress = 0f;

    boolean isCleaned;

    public void setMax(float max) {
        this.max = max;
        invalidate();
    }

    public void setProgress(float progress) {

        this.progress = progress;

        invalidate();
    }

    public HomeAnimView(Context context) {
        super(context);
        this.mContext = context;
    }

    public HomeAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public HomeAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public void setPercent(int percent) {
        mPercent = percent;
        invalidate();

    }

    String s;

    public void setStorageData(String s) {
        this.s = s;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenter = new PointF(w / 2f, h / 2f);

        int circleRadius = Math.min(w - dp2px(80), h - dp2px(80)) >> 1;
        int STROKE_WIDTH = circleRadius / 10;
        VALID_RADIUS = circleRadius - STROKE_WIDTH;
        circleRectF = new RectF(mCenter.x - VALID_RADIUS, mCenter.y - VALID_RADIUS,
                mCenter.x + VALID_RADIUS, mCenter.y + VALID_RADIUS);


        RADIANS_PER_X = (float) (Math.PI / VALID_RADIUS);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = dp2px(90);

        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = dp2px(90);

        }
        setMeasuredDimension(mWidth, mHeight);


    }


    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public void isCleaned(boolean isClean) {
        this.isCleaned = isClean;
        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isCleaned) {
            startArcColor = Color.parseColor("#FFFFFF");
            endArcColor = Color.parseColor("#EAEAFF");
            waveColor = 0xaa614FED;
        } else {
            startArcColor = Color.parseColor("#FFFFFF");
            endArcColor = Color.parseColor("#FFE9E6");
            waveColor = 0xaaFF5E35;
        }
        startProgressColor = Color.parseColor("#3F28EF");
        endProgressColor = Color.parseColor("#FCFBFF");
        drawArcs(canvas, startArcColor, endArcColor);
        if (showProgress) {
            drawProgress(canvas, endProgressColor, startProgressColor);
        }
        drawWave(canvas);
        if (showTopTxt) {
            drawTxt(canvas);
        }
    }

    boolean showTopTxt = true;

    public void setShowTopTxt(boolean showTopTxt) {
        this.showTopTxt = showTopTxt;
        invalidate();
    }

    private Rect textBounds = new Rect();

    private String used;
    private String all;

    public void setUsed(String used) {
        this.used = used;
    }

    public void setAll(String all) {
        this.all = all;
    }

    private void drawTxt(Canvas canvas) {
        String sTop = "";
        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setColor(Color.parseColor("#333333"));
            textPaint.setAntiAlias(true);
        }
        textPaint.setTextSize(dp2px(26));
        int n = (int) (progress * 100 / max);
        if (TextUtils.isEmpty(this.s1)) {
            sTop = n + "%";
        } else {
            sTop = s1;
        }
        //测量文字长度
        float w1 = textPaint.measureText(sTop);
        //测量文字高度
        textPaint.getTextBounds(sTop, 0, 1, textBounds);
        float h1 = textBounds.height();
        canvas.drawText(sTop, mCenter.x - w1 / 2, mCenter.y + h1 / 2 + dp2px(20), textPaint);

        textPaint.setTextSize(dp2px(15));
        String s3 = s;
        if (!TextUtils.isEmpty(s3)) {


            //测量文字长度
            float w3 = textPaint.measureText(s3);
            //测量文字高度
            textPaint.getTextBounds(s3, 0, 1, textBounds);
            float h3 = textBounds.height();
            canvas.drawText(s3, mCenter.x - w3 / 2, mCenter.y + dp2px(5) - dp2px(10) + h1 + h3 / 2, textPaint);
        }

    }

    Paint paint;

    private void drawWave(Canvas canvas) {
        if (progressPaint == null) {
            progressPaint = new Paint();
            progressPaint.setAntiAlias(true);
        }
        progressPaint.setColor(waveColor);


        canvas.drawPath(getWavePath(xOffset), progressPaint);
        canvas.drawPath(getWavePath(xOffset + dp2px(50)), progressPaint);
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
        }
        int colors[] = {Color.parseColor("#66FFFFFF"), Color.parseColor("#00FFFFFF"), Color.parseColor("#00FFFFFF")};
        LinearGradient linearGradient = new LinearGradient(0, 0, getWidth(), 0, colors, null, Shader.TileMode.CLAMP);

        paint.setShader(linearGradient);
        canvas.drawCircle(mCenter.x, mCenter.y, VALID_RADIUS, paint);
    }

    ValueAnimator valueAnimator;

    /**
     * 波形动画
     */
    private void initAnimation() {

        valueAnimator = ValueAnimator.ofInt(0, (int) (VALID_RADIUS * 2f));
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                xOffset = (int) animation.getAnimatedValue();

                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    public void startWaveAnimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initAnimation();
            }
        }, 200);

    }

    public void releaseWaveAnimation() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public void pauseWaveAnimation() {
        if (valueAnimator != null) {
            valueAnimator.pause();
        }
    }

    public void resumeWaveAnimation() {
        if (valueAnimator != null) {
            valueAnimator.resume();
        }
    }

    public int getGradient(float fraction, int startColor, int endColor) {
        if (fraction > 1) fraction = 1;
        int alphaStart = Color.alpha(startColor);
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaEnd = Color.alpha(endColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaDifference = alphaEnd - alphaStart;
        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaCurrent = (int) (alphaStart + fraction * alphaDifference);
        int redCurrent = (int) (redStart + fraction * redDifference);
        int blueCurrent = (int) (blueStart + fraction * blueDifference);
        int greenCurrent = (int) (greenStart + fraction * greenDifference);
        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }

    /**
     * 绘制外圈旋转渐变进度条
     *
     * @param canvas
     * @param progressStartColor
     * @param progressEndColor
     */
    Paint p;

    private void drawProgress(final Canvas canvas, int progressStartColor, int progressEndColor) {
        unitAngle = sweepAngle / 100.0f;
        if (p == null) {
            p = new Paint();
            p.setAntiAlias(true);
        }
        int strokeWidth = dp2px(5);
        p.setStrokeWidth(strokeWidth);

        p.setStyle(Paint.Style.STROKE);

        p.setStrokeCap(Paint.Cap.ROUND);
        RectF progressRectF = new RectF(strokeWidth + dp2px(20), strokeWidth + dp2px(20), mWidth - strokeWidth - dp2px(20), mHeight - strokeWidth - dp2px(20));

        for (int i = 0, end = (int) (curProgress * unitAngle); i <= end; i++) {
            p.setColor(getGradient(i / (float) end, progressStartColor, progressEndColor));
            canvas.drawArc(progressRectF,
                    startAngle + i + rotate,
                    1,
                    false,
                    p);
        }


    }

    ValueAnimator animator = ValueAnimator.ofInt(0, 360);

    public void startProgressAnim() {

        animator.setDuration(1500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotate = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    public void pauseProgressAnim() {
        animator.pause();

    }

    public void resumeProgressAnim() {
        animator.resume();
    }

    public void releaseProgressAnim() {
        animator.end();
    }


    /**
     * 获取水波曲线（包含圆弧部分）的Path.
     *
     * @param xOffset x方向像素偏移量.
     */
    private Path getWavePath(int xOffset) {
        if (wavePath == null) {
            wavePath = new Path();
        } else {
            wavePath.reset();
        }
        float[] startPoint = new float[2]; //波浪线起点
        float[] endPoint = new float[2]; //波浪线终点
        for (int i = 0; i <= VALID_RADIUS * 2; i += 2) {
            float x = mCenter.x - VALID_RADIUS + i;
            float y = (float) (mCenter.y + VALID_RADIUS * (1.0f + A) * 2 * (0.5f - progress / max)
                    + VALID_RADIUS * A * Math.sin((xOffset + i) * RADIANS_PER_X));
//只计算内圆内部的点，边框上的忽略
            if (calDistance(x, y, mCenter.x, mCenter.y) > VALID_RADIUS) {
                if (x < mCenter.x) {
                    continue; //左边框,继续循环
                } else {
                    break; //右边框,结束循环
                }
            }
//第1个点
            if (wavePath.isEmpty()) {
                startPoint[0] = x;
                startPoint[1] = y;
                wavePath.moveTo(x, y);
            } else {
                wavePath.lineTo(x, y);
            }
            endPoint[0] = x;
            endPoint[1] = y;
        }
        if (wavePath.isEmpty()) {
            if (progress / max >= 0.5f) {
//满格
                wavePath.moveTo(mCenter.x, mCenter.y - VALID_RADIUS);
                wavePath.addCircle(mCenter.x, mCenter.y, VALID_RADIUS, Path.Direction.CW);
            } else {
//空格
                return wavePath;
            }
        } else {
//添加圆弧部分
            float startDegree = calDegreeByPosition(startPoint[0], startPoint[1]); //0~180
            float endDegree = calDegreeByPosition(endPoint[0], endPoint[1]); //180~360
            wavePath.arcTo(circleRectF, endDegree - 360, startDegree - (endDegree - 360));
        }
        return wavePath;
    }

    //根据当前位置，计算出进度条已经转过的角度。
    private float calDegreeByPosition(float currentX, float currentY) {
        float a1 = (float) (Math.atan(1.0f * (mCenter.x - currentX) / (currentY - mCenter.y)) / Math.PI * 180);
        if (currentY < mCenter.y) {
            a1 += 180;
        } else if (currentY > mCenter.y && currentX > mCenter.x) {
            a1 += 360;
        }
        return a1 + 90;
    }

    private float calDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    Paint pArcs;

    /**
     * 绘制两个圆环
     *
     * @param canvas
     */
    private void drawArcs(Canvas canvas, int startArcColor, int endArcColor) {
        if (pArcs == null) {
            pArcs = new Paint();
            pArcs.setAntiAlias(true);
        }

        int strokeWidth = dp2px(20);
        pArcs.setStrokeWidth(strokeWidth);

        pArcs.setStyle(Paint.Style.STROKE);
        int[] colors = {startArcColor, endArcColor};
        float[] positions = {0.83f, 1f};
        //绘制环形渐变色
        RadialGradient linearGradient = new RadialGradient(getWidth() / 2f, getHeight() / 2f, mHeight / 2f,
                colors, positions, Shader.TileMode.REPEAT);
        pArcs.setShader(linearGradient);
        RectF firstRectF = new RectF(strokeWidth + dp2px(20), strokeWidth + dp2px(20), mWidth - strokeWidth - dp2px(20), mHeight - strokeWidth - dp2px(20));
        RectF secondRectF = new RectF(strokeWidth, strokeWidth, mWidth - strokeWidth, mHeight - strokeWidth);

        float percent = mPercent / 100f;

        final float fill = 360 * percent;


        pArcs.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawArc(secondRectF, startAngel, fill, false, pArcs);
        RadialGradient l2 = new RadialGradient(getWidth() / 2f, getHeight() / 2f, (mHeight - dp2px(40)) / 2f,
                colors, positions, Shader.TileMode.REPEAT);
        pArcs.setShader(l2);
        canvas.drawArc(firstRectF, startAngel, fill, false, pArcs);
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    //标记View是否已经销毁
    private boolean detached = false;


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        detached = true;
    }

    public void setCleanStatus(boolean cleanValidInSession) {
        this.isCleaned = cleanValidInSession;
        invalidate();
    }

    public String getS1() {
        return s1;
    }

    String s1;

    public void setTopTxt(String s1) {
        this.s1 = s1;
        invalidate();
    }
}
