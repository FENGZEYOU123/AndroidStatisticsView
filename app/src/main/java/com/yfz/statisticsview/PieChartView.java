package com.yfz.statisticsview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * 作者：游丰泽
 * 简介：统计view-饼状图-用直观的方式展示数据内容
 * android需求,交流请加wx：yfz_oom
 */
public class PieChartView extends View {
    private Context mContext;
    //画笔
    private Paint mPaint=new Paint();
    //记录绘制数据，key名字,value数量
    private ArrayList<ColumnBarChartView.ColumnDataFrom> mArray=new ArrayList<>();
    //柱状宽度
    private float mWidth=0;
    //柱状高度
    private float mHeight=0;
    //柱状位置Rect
    private RectF mRectF = new RectF();
    //数据名字位置Rect
    private Rect mTextRect=new Rect();
    /**
     * 设置柱状间距
     */
    private int mMargin=20;
    /**
     * 设置是否显示数据（柱状名字-数值）
     */
    private boolean mDisplayData =true;
    /**
     * 设置文字颜色
     */
    private int mTextColor= Color.BLACK;

    public PieChartView(Context context) {
        super(context);
        initial(context);
    }
    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.ColumnBarChartView);
        //设置柱状的间距
        mMargin=typedArray.getInteger(R.styleable.ColumnBarChartView_ColumnBarChart_margin,mMargin);
        mDisplayData =typedArray.getBoolean(R.styleable.ColumnBarChartView_ColumnBarChart_displayData, mDisplayData);
        mTextColor =typedArray.getInteger(R.styleable.ColumnBarChartView_ColumnBarChart_textColor, mTextColor);
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
                onDrawNumber(canvas,i); //画柱状数值
            }
            onDrawColumn(canvas,i); //画柱状图形
        }
    }

    //绘制-柱状名字
    private void onDrawName(Canvas canvas,int i){
        mPaint.setTextSize(dip2px(mContext, (float) Math.sqrt(mWidth)));
        mPaint.getTextBounds(mArray.get(i).mMame, 0, mArray.get(i).mMame.length(), mTextRect); //计算文字位置,处于柱状宽度中间
        mPaint.setColor(mTextColor);
        canvas.drawText(   //柱状名字
                mArray.get(i).mMame,
                (mRectF.left+mRectF.right)/2 - (mTextRect.left+mTextRect.right)/2,
                mRectF.bottom-(mTextRect.bottom-mTextRect.top)/2,
                mPaint);

    }
    //绘制-柱状数值
    private void onDrawNumber(Canvas canvas,int i){
        mPaint.setTextSize(dip2px(mContext, (float) Math.sqrt(mWidth)));
        mPaint.getTextBounds(String.valueOf(mArray.get(i).mNumber), 0, String.valueOf(mArray.get(i).mNumber).length(), mTextRect); //计算文字位置,处于柱状宽度中间
        mPaint.setColor(mTextColor);
        canvas.drawText(   //柱状名字
                String.valueOf(mArray.get(i).mNumber),
                (mRectF.left+mRectF.right)/2 - (mTextRect.left+mTextRect.right)/2,
                mRectF.top,
                mPaint);

    }
    //绘制-柱状图形
    private void onDrawColumn(Canvas canvas,int i){
        if(mDisplayData) { //如果设置了展示名字，则绘制柱状图形的位置要在文字之上
            mRectF.bottom=mRectF.bottom-dip2px(mContext,(mTextRect.bottom-mTextRect.top)); //柱状底部位置=组件底部-文字高度
            mRectF.top= mRectF.bottom-mHeight*mArray.get(i).mNumber +dip2px(mContext,(mTextRect.bottom-mTextRect.top+2)) ; //柱状高度=数值的平均高度值*柱状数值
        }
        Drawable drawable;
        try{
            drawable=getResources().getDrawable(mArray.get(i).mColorOrDrawable);
            drawable.setBounds((int)mRectF.left,(int)mRectF.top,(int)mRectF.right,(int)mRectF.bottom);
            drawable.draw(canvas);
        }catch (Exception e){
            mPaint.setColor(mArray.get(i).mColorOrDrawable); //柱状颜色
            canvas.drawRect(mRectF,mPaint);
        }


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
    public void addColumnData(ColumnBarChartView.ColumnDataFrom columnDataFrom){
        if(null != mArray){
            if(!mArray.contains(columnDataFrom)) {
                mArray.add(columnDataFrom);
                refreshUI();
            }
        }
    }
    /**
     * 向外提供修改数据的方法
     */
    public void editColumnData(String ColumnName,String name,float number,int color){
        if(null != mArray){
            if(mArray.contains(ColumnName)){
                for(int i=0;i<mArray.size();i++){
                    if(mArray.get(i).mMame.equals(ColumnName)){
                        mArray.get(i).mMame=name;
                        mArray.get(i).mNumber=number;
                        mArray.get(i).mColorOrDrawable =color;
                    }
                }
                refreshUI();
            }
        }
    }
    /**
     * 向外提供删除数据的方法
     */
    public void deleteColumnData(String ColumnName){
        if(null != mArray){
            if(mArray.contains(ColumnName)){
                mArray.remove(ColumnName);
                refreshUI();
            }
        }
    }
    /**
     * 向外提供删除数据的方法
     */
    public void deleteColumnData(int ColumnIndex){
        if(null != mArray){
            if( mArray.size()>ColumnIndex){
                if(null != mArray.get(ColumnIndex) ){
                    mArray.remove(ColumnIndex);
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
        int mColorOrDrawable;
        public ColumnDataFrom(String name,float number,int colorOrDrawable){
            mMame=name;
            mNumber=number;
            mColorOrDrawable =colorOrDrawable;
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
        for(ColumnBarChartView.ColumnDataFrom columnDataFrom : mArray){
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
