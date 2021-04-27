package com.yfz.statisticsview;

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

import java.util.ArrayList;
/**
 * 作者：游丰泽
 * 简介：统计图-柱状图-用直观的方式展示数据内容
 * android交流需求请加wx：yfz_oom
 */
public class ColumnBarChartView extends View {
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
    //柱状名字显示
    private boolean mDisplayName=false;
    public ColumnBarChartView(Context context) {
        super(context);
        initial();
    }
    public ColumnBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.ColumnBarChartView);
        //设置柱状的间距
        mMargin=typedArray.getInteger(R.styleable.ColumnBarChartView_ColumnBarChart_margin,mMargin);
        mDisplayName=typedArray.getBoolean(R.styleable.ColumnBarChartView_ColumnBarChart_displayName,mDisplayName);
        initial();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void initial(){
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setStyle(Paint.Style.FILL);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mWidth  = ( getWidth() - (mMargin*(getSize()-1)) ) / getSize(); //计算每个柱状宽度
        mHeight = ( getHeight() / getNumber() ); //计算柱状数值的平均高度值
        for(int i=0;i<mArray.size();i++){
            if(mDisplayName){ //如果设置了展示名字，则绘制名字
                onDrawName(canvas,i); //画柱状名字
            }
                onDrawColumn(canvas,i); //画柱状图形
        }
    }

    //绘制-柱状名字
    private void onDrawName(Canvas canvas,int i){

    }
    //绘制-柱状图形
    private void onDrawColumn(Canvas canvas,int i){
        mPaint.setColor(mArray.get(i).mColor); //柱状颜色
        mRectF.left= i*(mWidth+mMargin);
        mRectF.right= mRectF.left+mWidth;
        mRectF.bottom=getHeight();
        mRectF.top= mRectF.bottom-mHeight*mArray.get(i).mNumber ; //柱状高度=数值的平均高度值*柱状数值
        canvas.drawRect(mRectF,mPaint);
    }

    /**
     * 是否显示柱状名称
     */
    public void displayName(boolean isDisplay){
        mDisplayName=isDisplay;
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


}
