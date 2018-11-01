package br.com.food4fit;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.food4fit.food4fit.R;

public class AcompanhamentoFragment extends Fragment {
    private final String[] diasSemana = {"", "", "", "", "", "", "", ""};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acompanhamento, container, false);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            diasSemana[i + 1] =  calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Food4fitApp.LOCALE);
        }

        LineChart chart = view.findViewById(R.id.grafico_acompanhamento);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setValueFormatter((value, axis) -> diasSemana[(int) value]);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getLegend().setEnabled(false);

        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 0));
        values.add(new Entry(1, 3000));
        values.add(new Entry(2, 3000));
        values.add(new Entry(3, 2000));
        values.add(new Entry(4, 5000));
        values.add(new Entry(5, 3000));
        values.add(new Entry(6, 2000));
        values.add(new Entry(7, 5000));

        LineDataSet lineDataSet = new LineDataSet(values, "DataSet 1");
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setDrawValues(false);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        lineDataSet.setFormSize(15.f);
        lineDataSet.setValueTextSize(9f);
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        chart.setData(data);

        chart.animateX(500);
        return view;
    }
}