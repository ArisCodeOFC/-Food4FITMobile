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

import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.ItemAcompanhamento;

public class AcompanhamentoFragment extends Fragment {
    private final String[] diasSemana = {"", "", "", "", "", "", "", ""};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acompanhamento, container, false);

        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 0));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            diasSemana[i + 1] =  calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Food4fitApp.LOCALE);
            ItemAcompanhamento acompanhamento = AppDatabase.getDatabase(getContext()).getAcompanhamentoDAO().selecionarDia(calendar.getTime());
            if (acompanhamento == null) {
                values.add(new Entry(i + 1, 0));
            } else {
                values.add(new Entry(i + 1, (int) acompanhamento.getCalorias()));
            }
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

        LineDataSet lineDataSet = new LineDataSet(values, "Calorias");
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueFormatter((v, entry, i, viewPortHandler) -> v == 0 ? "" : String.valueOf(v));
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[] {10f, 5f}, 0f));
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