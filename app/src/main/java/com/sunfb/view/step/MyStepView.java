package com.sunfb.view.step;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.sunfb.view.R;

public class MyStepView extends View {
    private Paint mInnerPaint,mOutPaint,mStepPaint;

    private int mInnerColor= Color.RED;
    private int mOutColor=Color.BLUE;
    private int mBorderWidth=5;
    private int mStepTextColor=Color.RED;
    private int mStepTextSize=16;

    private int mCurrentStep=2000;
    private int mMaxStep=4000;

    public MyStepView(Context context) {
        this(context,null);
    }

    public MyStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }


    @SuppressLint("ResourceAsColor")
    public MyStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化属性
        TypedArray typedArray =context.obtainStyledAttributes(attrs,R.styleable.MyStepView);
        mInnerColor =typedArray.getColor(R.styleable.MyStepView_innerColor,mInnerColor);
        mOutColor=typedArray.getColor(R.styleable.MyStepView_outColor,mOutColor);
        mBorderWidth = (int) typedArray.getDimension(R.styleable.MyStepView_borderWidth,mBorderWidth);
        mStepTextColor=typedArray.getColor(R.styleable.MyStepView_stepTextColor,mStepTextColor);
        mStepTextSize=typedArray.getDimensionPixelOffset(R.styleable.MyStepView_stepTextSize,mStepTextSize);
        //自愿一定要回收
        typedArray.recycle();
        //外侧圆弧 Paint.ANTI_ALIAS_FLAG抗锯齿
        mOutPaint=new Paint();
        mOutPaint.setStrokeWidth(mBorderWidth);
        mOutPaint.setColor(mOutColor);
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutPaint.setAntiAlias(true);
        mOutPaint.setStrokeJoin(Paint.Join.ROUND);

        //内侧圆弧
        mInnerPaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerPaint.setStrokeJoin(Paint.Join.ROUND);
        //步数文本
        mStepPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mStepPaint.setColor(mStepTextColor);
        mStepPaint.setTextSize(mStepTextSize);


    }

    /**
     * 测量 确定大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //int mode = MeasureSpec.getMode(widthMeasureSpec);
        //UNSPECIFIED ，EXACTLY，AT_MOST
        //1.UNSPECIFIED 尽可能的大，ListView，RecyclerView，ScrollView 这里灰用到
        // 2.EXACTLY 确切的 100dp fill_parent,match_parent
        // 3. AT_MOST wrap_content
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width,height),Math.min(width,height));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX=getWidth()/2;
        int radius =centerX-mBorderWidth;
        //外部圆弧
        RectF rectF =new RectF(centerX-radius,centerX-radius,centerX+radius,centerX+radius);
        canvas.drawArc(rectF,135,270,false,mOutPaint);
        //内部圆弧
        int  sweepAngle =270*mCurrentStep/mMaxStep;
        canvas.drawArc(rectF,135,sweepAngle,false,mInnerPaint);
        //当前步数
        String stepText=mCurrentStep+"";
        Rect textBounds=new Rect();
        mStepPaint.getTextBounds(stepText,0,stepText.length(),textBounds);
        float dx =getWidth()/2-textBounds.width()/2;
        float baseline =getBaseline2(mStepPaint);
        /**
         * TODO 按照思维上说 dy=getHeight()/2+baseline; 由于圆弧的在外切正方形里 我们在设置宽高的时候是取的宽高最小值
         * 而布局中设置的属性是android:layout_width="match_parent"，android:layout_height="match_parent" 为此dy=dy=getWidth()/2+baseline
          */
        float dy=getWidth()/2+baseline;
        canvas.drawText(stepText,dx,dy,mStepPaint);
        invalidate();
    }

    public void setCurrentStep(int currentStep) {
        this.mCurrentStep = currentStep;
    }
    public void setMaxStep(int maxStep){
        this.mMaxStep=maxStep;
    }
    //getBaseline() getBaseline2()返回值是相同的基线值
    /**
     * 计算绘制文字时的基线到中轴线的距离
     *  参考文献地址：https://www.jianshu.com/p/057ce6b81c52 感谢该作者
     * @param p
     * @return 基线和centerY的距离
     */
    public  float getBaseline(Paint p) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        return (fontMetrics.descent - fontMetrics.ascent) / 2 -fontMetrics.descent;
    }

    /**
     * 计算绘制文字时的基线到中轴线的距离
     *  参考文献地址：https://www.jianshu.com/p/057ce6b81c52 感谢该作者
     * @param p
     * @return 基线和centerY的距离
     */
    public  float getBaseline2(Paint p) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        return (fontMetrics.bottom - fontMetrics.top) / 2 -fontMetrics.bottom;
    }

}
