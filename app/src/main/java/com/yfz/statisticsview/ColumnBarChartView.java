package com.yfz.statisticsview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.ArrayList;
/**
 * 作者：游丰泽
 * 简介：统计view-柱状图-用直观的方式展示数据内容
 * android需求,交流请加wx：yfz_oom
 */
public class ColumnBarChartView extends View {
    private Context mContext;
    //画笔
    private Paint mPaint=new Paint();
    //记录绘制数据，key名字,value数量
    private ArrayList<ColumnDataFrom> mArray=new ArrayList<>();
    //柱状间距
    private int mMargin=20;
    //柱状宽度
    private float mWidth=0;
    //柱状高度
    private float mHeight=0;
    //柱状位置Rect
    private RectF mRectF = new RectF();
    //数据名字位置Rect
    private Rect mTextRect=new Rect();
    //数据显示
    private boolean mDisplayData =false;
    //数据文字大小
    private float mTextSize=20;
    //数据文字颜色
    private int mTextColor= Color.BLACK;
    public ColumnBarChartView(Context context) {
        super(context);
        initial(context);
    }
    public ColumnBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.ColumnBarChartView);
        //设置柱状的间距
        mMargin=typedArray.getInteger(R.styleable.ColumnBarChartView_ColumnBarChart_margin,mMargin);
        mDisplayData =typedArray.getBoolean(R.styleable.ColumnBarChartView_ColumnBarChart_displayData, mDisplayData);
        mTextColor =typedArray.getInteger(R.styleable.ColumnBarChartView_ColumnBarChart_textColor, mTextColor);
        mTextSize =typedArray.getFloat(R.styleable.ColumnBarChartView_ColumnBarChart_textSize, mTextSize);
        initial(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void initial(Context context){
        mContext=context;
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mWidth  = ( getWidth() - (mMargin*(getSize()-1)) ) / getSize(); //计算每个柱状宽度
        mHeight = ( getHeight() / getNumber() ); //计算柱状数值的平均高度值
        for(int i=0;i<mArray.size();i++){
            mRectF.left= i*(mWidth+mMargin);
            mRectF.right= mRectF.left+mWidth;
            mRectF.bottom=getHeight();
            mRectF.top= mRectF.bottom-mHeight*mArray.get(i).mNumber ; //柱状高度=数值的平均高度值*柱状数值
            if(mDisplayData){ //如果设置了展示名字，则绘制名字
                onDrawName(canvas,i); //画柱状名字
            }
            onDrawColumn(canvas,i); //画柱状图形

        }
    }

    //绘制-柱状名字
    private void onDrawName(Canvas canvas,int i){
        mPaint.setTextSize(dip2px(mContext, (float) Math.sqrt(mWidth)));
        mPaint.getTextBounds(mArray.get(i).mMame, 0, mArray.get(i).mMame.length(), mTextRect); //计算文字位置,处于柱状中间
        mPaint.setColor(mTextColor);
        canvas.drawText(   //柱状名字
                mArray.get(i).mMame,
                (mRectF.left+mRectF.right)/2 - (mTextRect.left+mTextRect.right)/2,
                mRectF.bottom-(mTextRect.bottom-mTextRect.top)/2,
                mPaint);

    }
    //绘制-柱状图形
    private void onDrawColumn(Canvas canvas,int i){
        if(mDisplayData) { //如果设置了展示名字，则绘制柱状图形的位置要在文字之上
            mRectF.bottom=mRectF.bottom-dip2px(mContext,(mTextRect.bottom-mTextRect.top));
            mRectF.top=mRectF.bottom-mRectF.top;
        }
        mPaint.setColor(mArray.get(i).mColor); //柱状颜色
        canvas.drawRect(mRectF,mPaint);
    }

    /**
     * 是否显示柱状名称
     */
    public void displayName(boolean isDisplay){
        mDisplayData =isDisplay;
    }
    /**
     * 向外提供添加数据的方法,如果数据名字一样则被替换
     */
    public void addColumnData(ColumnDataFrom columnDataFrom){
        if(null != mArray){
            if(!mArray.contains(columnDataFrom)) {
                mArray.add(columnDataFrom);
                refreshUI();
            }
        }
    }
    /**
     * 向外提供删除数据的方法
     */
    public void deleteColumnData(String name){
        if(null != mArray){
            if(mArray.contains(name)){
                mArray.remove(name);
                refreshUI();
            }
        }
    }
    /**
     * 向外提供删除数据的方法
     */
    public void deleteColumnData(int index){
        if(null != mArray){
            if( mArray.size()>index ){
               if(null != mArray.get(index) ){
                    mArray.remove(index);
                    refreshUI();
                }
            }
        }
    }
    /**
     * 柱状数据表单
     */
    public static class ColumnDataFrom {
        String mMame;
        float mNumber;
        int mColor;
        public ColumnDataFrom(String name,float number,int color){
            mMame=name;
            mNumber=number;
            mColor=color;
        }
    }
    //刷新UI
    private void refreshUI(){
        this.invalidate();
    }

    /**
     * 获取柱状数量
     * @return
     */
    public int getSize(){
        if(null!=mArray){
            return mArray.size();
        }else {
            return 0;
        }
    }

    /**
     * 获取所有柱状的数值总数
     * @return
     */
    public float getNumber(){
        float totalNumber=0;
        for(ColumnDataFrom columnDataFrom : mArray){
            totalNumber= totalNumber+ columnDataFrom.mNumber;
        }
        return totalNumber;
    }

    //大小转换为px
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
