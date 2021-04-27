package com.yfz.statisticsview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ColumnBarChartView mColumnBarChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
    }

    private void initial() {
        mColumnBarChartView = findViewById(R.id.columnBarChartView);
        mColumnBarChartView.addColumnData(new ColumnBarChartView.ColumnDataFrom("不及格", 5, Color.YELLOW));
        mColumnBarChartView.addColumnData(new ColumnBarChartView.ColumnDataFrom("及格", 15, Color.RED));
        mColumnBarChartView.addColumnData(new ColumnBarChartView.ColumnDataFrom("优秀", 10, Color.BLUE));
    }

    public void addRandomData(View view) {
        int number = (int)(Math.random()*40+5);
        mColumnBarChartView.addColumnData(new ColumnBarChartView.ColumnDataFrom("随机的 " + number, number, getRandColorCode()));
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