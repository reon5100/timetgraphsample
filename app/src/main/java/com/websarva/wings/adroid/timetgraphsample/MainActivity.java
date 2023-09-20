package com.websarva.wings.adroid.timetgraphsample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.websarva.wings.adroid.timetgraphsample.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LineChart chart;
    private LineDataSet dataSet;
    private Timer timer;
    private Random random;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chart = findViewById(R.id.line_chart);
        dataSet = new LineDataSet(null, "Real-time Data");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setEnabled(false);

        random = new Random();
        handler = new Handler(Looper.getMainLooper());
        startRealTimeUpdates();
    }

    private void startRealTimeUpdates() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // 新しいデータを生成または取得
                float newValue = random.nextFloat() * 100; // ランダムな値を生成

                // 新しいデータをグラフに追加
                LineData data = chart.getData();
                if (data != null) {
                    LineDataSet dataSet = (LineDataSet) data.getDataSetByIndex(0);
                    if (dataSet == null) {
                        dataSet = createDataSet();
                        data.addDataSet(dataSet);
                    }

                    // グラフに新しいエントリを追加
                    data.addEntry(new Entry(dataSet.getEntryCount(), newValue), 0);
                    data.notifyDataChanged();

                    // グラフの描画を更新
                    chart.notifyDataSetChanged();
                    chart.setVisibleXRangeMaximum(30); // グラフ上に表示するエントリ数を制限
                    chart.moveViewToX(data.getEntryCount()); // グラフをスクロール
                }
            }
        }, 0, 500); // 0.1秒ごとにデータを更新
    }

    private LineDataSet createDataSet() {
        LineDataSet dataSet = new LineDataSet(null, "Real-time Data");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        return dataSet;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
