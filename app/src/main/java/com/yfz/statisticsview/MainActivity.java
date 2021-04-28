package com.yfz.statisticsview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;
/**
 * 作者：游丰泽
 * 简介：统计view-柱状图-用图形方式更直观的展示数据内容
 */
public class MainActivity extends AppCompatActivity {
    private ColumnBarChartView mColumnBarChartView;
    private PieChartView mPieChatView;
    private ViewPager mViewPager;
    private LayoutInflater layoutInflater;
    private ArrayList<View> mList=new ArrayList<>();
    private View mViewColumn,mViewPie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
    }

    private void initial() {
        mViewPager=findViewById(R.id.viewPager);
        layoutInflater=getLayoutInflater().from(this);
        mViewColumn=layoutInflater.inflate(R.layout.columb_bar_chat_view,null);
        mViewPie=layoutInflater.inflate(R.layout.pie_bar_chat_view,null);
        mList.add(mViewColumn);
        mList.add(mViewPie);
        mViewPager.setAdapter(new ViewPagerAdapter());
        mViewPager.setOffscreenPageLimit(mList.size()-1);
        mColumnBarChartView = mViewColumn.findViewById(R.id.columnBarChartView);
        mColumnBarChartView.addColumnData(new ColumnBarChartView.ColumnDataFrom("不及格", 5, Color.YELLOW));
        mColumnBarChartView.addColumnData(new ColumnBarChartView.ColumnDataFrom("及格", 15, Color.RED));
        mColumnBarChartView.addColumnData(new ColumnBarChartView.ColumnDataFrom("优秀", 10, Color.BLUE));

        mPieChatView=mViewPie.findViewById(R.id.pieChatView);
        mPieChatView.addColumnData(new ColumnBarChartView.ColumnDataFrom("不及格", 5, Color.YELLOW));
        mPieChatView.addColumnData(new ColumnBarChartView.ColumnDataFrom("及格", 15, Color.RED));
        mPieChatView.addColumnData(new ColumnBarChartView.ColumnDataFrom("优秀", 10, Color.BLUE));
    }

    class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }
    }

    public void addRandomData(View view) {
        int number = (int)(Math.random()*40+5);
        mColumnBarChartView.addColumnData(new ColumnBarChartView.ColumnDataFrom("随机 " + number, number, getRandColorCode()));
    }

    public void deleteRandomData(View view) {
        Random random = new Random();
        int number =  random.nextInt(mColumnBarChartView.getSize()-1);
        mColumnBarChartView.deleteColumnData(number);
    }

    private static int getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        int a =  Color.parseColor("#"+r + g + b);
        return  a;

    }
}