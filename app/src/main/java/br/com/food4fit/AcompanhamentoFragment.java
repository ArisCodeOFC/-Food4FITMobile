package br.com.food4fit;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Dieta;
import br.com.food4fit.model.ItemAcompanhamento;
import br.com.food4fit.model.Refeicao;
import br.com.food4fit.model.Usuario;

public class AcompanhamentoFragment extends Fragment {
    private final String[] diasSemana = {"", "", "", "", "", "", "", ""};
    private LineChart graficoCaloriasDia;
    private PieChart graficoDieta;
    private Usuario usuario;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acompanhamento, container, false);
        if (getActivity() != null) {
            usuario = ((Food4fitApp) getActivity().getApplication()).getUsuario();
            graficoCaloriasDia = view.findViewById(R.id.grafico_acompanhamento);
            graficoDieta = view.findViewById(R.id.grafico_dieta);
            montarGraficoCaloriasDia();
            montarGraficoDieta();
        }

        return view;
    }

    private void montarGraficoCaloriasDia() {
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

        graficoCaloriasDia.getDescription().setEnabled(false);
        graficoCaloriasDia.setTouchEnabled(false);
        graficoCaloriasDia.setDrawGridBackground(false);
        graficoCaloriasDia.setDragEnabled(false);
        graficoCaloriasDia.setScaleEnabled(false);
        graficoCaloriasDia.setPinchZoom(false);
        graficoCaloriasDia.getAxisRight().setDrawLabels(false);
        graficoCaloriasDia.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        graficoCaloriasDia.getXAxis().setValueFormatter((value, axis) -> diasSemana[(int) value]);
        graficoCaloriasDia.getAxisLeft().setAxisMinimum(0);
        graficoCaloriasDia.getLegend().setEnabled(false);
        graficoCaloriasDia.setNoDataText("Sem dados disponíveis");
        graficoCaloriasDia.setNoDataTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
        graficoCaloriasDia.setNoDataTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

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
        graficoCaloriasDia.setData(data);
        graficoCaloriasDia.animateX(500);
    }

    private void montarGraficoDieta() {
        graficoDieta.getDescription().setEnabled(false);
        graficoDieta.setTouchEnabled(false);
        graficoDieta.getLegend().setEnabled(false);
        graficoDieta.setDrawHoleEnabled(false);
        graficoDieta.setNoDataText("Sem dados disponíveis");
        graficoDieta.setNoDataTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
        graficoDieta.setNoDataTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        Dieta dieta = AppDatabase.getDatabase(getContext()).getDietaDAO().getDietaAtiva(usuario.getId());
        if (dieta != null && dieta.getCalorias() != 0) {
            ArrayList<PieEntry> values = new ArrayList<>();

            double totalCalorias = dieta.getCalorias();
            for (Refeicao refeicao : dieta.getRefeicoes()) {
                double porcentagem = refeicao.getCalorias() * 100 / totalCalorias;
                values.add(new PieEntry((float) porcentagem, refeicao.getData().getTitulo()));
            }

            PieDataSet pieDataSet = new PieDataSet(values, "Calorias");
            pieDataSet.setDrawValues(true);
            pieDataSet.setValueTextSize(13f);
            pieDataSet.setValueTextColor(Color.WHITE);
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            pieDataSet.setValueFormatter(new PercentFormatter());

            PieData data = new PieData(pieDataSet);
            graficoDieta.setData(data);
            graficoDieta.animateX(500);
        }
    }
}