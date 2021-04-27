package com.yfz.statisticsview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ColumnBarChartView mColumnBarChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
    }
    private void initial(){
        mColumnBarChartView=findViewById(R.id.columnBarChartView);
        mColumnBarChartView.addColumnData(new ColumnBarChartView.ColumnDataFrom("不及格",5, Color.YELLOW));
        mColumnBarChartView.addColumnData(new ColumnBarChartView.ColumnDataFrom("及格",15,Color.RED));
        mColumnBarChartView.addColumnData(new ColumnBarChartView.ColumnDataFrom("优秀",10,Color.BLUE));
    }
}