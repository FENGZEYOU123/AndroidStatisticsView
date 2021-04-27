package com.yfz.statisticsview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
    private int mMargin=10;
    //柱状宽度
    private int mWidth=0;
    //柱状高度
    private int mHeight=0;
    //柱状位置Rect
    private Rect mRect = new Rect();
    public ColumnBarChartView(Context context) {
        super(context);
        initial();
    }
    public ColumnBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initial();
    }
    public ColumnBarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial();
    }

    private void initial(){
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mWidth  = (int) ( getWidth() - (mMargin*(getSize()-1)) ) / getSize();
        mHeight = (int) ( getHeight() / getNumber() );
        for(int i=0;i<mArray.size();i++){
            mPaint.setColor(mArray.get(i).mColor);
            mRect.left= i*(mWidth+mMargin);
            mRect.right= mRect.left+mWidth;
            mRect.bottom=getHeight();
            mRect.top=(int) ( mHeight*mArray.get(i).mNumber );
            canvas.drawRect(mRect,mPaint);
        }
    }

    /**
     * 向外提供添加数据的方法,如果数据名字一样则被替换
     */
    public void addColumnData(ColumnDataFrom columnDataFrom){
        if(null != mArray){
            mArray.add(columnDataFrom);
            refreshUI();
        }
    }
    /**
     * 向外提供删除数据的方法
     */
    public void removeColumnData(String name){
        if(null != mArray){
            if(mArray.contains(name)){
                mArray.remove(name);
                refreshUI();
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
    //获取总柱状数量
    private int getSize(){
        if(null!=mArray){
            return mArray.size();
        }else {
            return 0;
        }
    }
    //获取所有数值总和
    private float getNumber(){
        float totalNumber=0;
        for(ColumnDataFrom columnDataFrom : mArray){
            totalNumber= totalNumber+ columnDataFrom.mNumber;
        }
        return totalNumber;
    }


}
