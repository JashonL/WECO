package com.olp.weco.ui.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.olp.weco.R;
import com.olp.weco.utils.ValueUtil;

import java.util.List;

public class MultipleChartMarkView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue;

    public MultipleChartMarkView(Context context) {
        super(context, R.layout.layout_multiple_mark_view);
        tvDate = findViewById(R.id.tv_date);
        tvValue = findViewById(R.id.tv_value);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //展示自定义X轴值 后的X轴内容
        Chart chart = getChartView();
        XAxis xAxis = chart.getXAxis();
        String xLabel = xAxis.getValueFormatter().getFormattedValue(e.getX(), xAxis);
        tvDate.setText(xLabel);

        if (chart instanceof LineChart) {
            LineData lineData = ((LineChart) chart).getLineData();
            //获取到图表中的所有曲线
            List<ILineDataSet> dataSetList = lineData.getDataSets();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < dataSetList.size(); i++) {
                LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                float y = dataSet.getValues().get((int) e.getX()).getY();
                String value = ValueUtil.INSTANCE.roundDouble2String(y, 2);

                sb.append(dataSet.getLabel()).append(" ").append(value);
                if (i != dataSetList.size() - 1) {
                    sb.append("\n");
                }
                tvValue.setText(sb.toString());
            }
        } else if ((chart instanceof BarChart)) {
            BarData barData = ((BarChart) chart).getBarData();
            //获取到图表中的所有曲线
            List<IBarDataSet> dataSets = barData.getDataSets();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < dataSets.size(); i++) {
                BarDataSet dataSet = (BarDataSet) dataSets.get(i);
                //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                float y = dataSet.getValues().get((int) e.getX()).getY();
                String value = ValueUtil.INSTANCE.roundDouble2String(y, 2);

                sb.append(dataSet.getLabel()).append(" ").append(value);
                if (i != dataSets.size() - 1) {
                    sb.append("\n");
                }
                tvValue.setText(sb.toString());
            }
        }


        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
