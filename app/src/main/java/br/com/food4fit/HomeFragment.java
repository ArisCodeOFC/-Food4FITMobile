package br.com.food4fit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.food4fit.adapter.DietaAtivaRefeicaoAdapter;
import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Dieta;
import br.com.food4fit.model.HistoricoAlimentacao;
import br.com.food4fit.model.Refeicao;
import br.com.food4fit.model.Usuario;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity activity = ((MainActivity) getActivity());
        if (activity != null && getContext() != null) {
            Toolbar toolbar = view.findViewById(R.id.toolbar);
            activity.configureToolbar(toolbar);
            activity.setTitle("Minha dieta");

            TextView txtSemDietaAtiva = view.findViewById(R.id.txt_sem_dieta_ativa);
            RecyclerView rvDietaAtiva = view.findViewById(R.id.rv_dieta_ativa);
            LinearLayout layoutDietaAtiva = view.findViewById(R.id.layout_dieta_ativa);
            TextView txtCalorias = view.findViewById(R.id.txt_dieta_ativa_calorias);
            TextView txtCarboidratos = view.findViewById(R.id.txt_dieta_ativa_carboidratos);
            TextView txtGorduras = view.findViewById(R.id.txt_dieta_ativa_gorduras);
            TextView txtData = view.findViewById(R.id.txt_dieta_ativa_data);

            rvDietaAtiva.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            rvDietaAtiva.setLayoutManager(new LinearLayoutManager(getContext()));

            Usuario usuario = ((Food4fitApp) getActivity().getApplication()).getUsuario();
            Dieta dieta = AppDatabase.getDatabase(getContext()).getDietaDAO().getDietaAtiva(usuario.getId());
            if (dieta == null) {
                txtSemDietaAtiva.setVisibility(View.VISIBLE);
                layoutDietaAtiva.setVisibility(View.GONE);
            } else {
                txtSemDietaAtiva.setVisibility(View.GONE);
                layoutDietaAtiva.setVisibility(View.VISIBLE);

                List<Refeicao> refeicoes = new ArrayList<>(dieta.getRefeicoes());
                Collections.sort(refeicoes, (refeicao1, refeicao2) -> refeicao1.getData().getHorario().compareTo(refeicao2.getData().getHorario()));

                List<HistoricoAlimentacao> historico = AppDatabase.getDatabase(getContext()).getHistoricoAlimentacaoDAO().getHistoricoDia();
                DietaAtivaRefeicaoAdapter adapter = new DietaAtivaRefeicaoAdapter(getContext(), refeicoes, historico);
                rvDietaAtiva.setAdapter(adapter);

                txtCalorias.setText(String.format(Food4fitApp.LOCALE, "%.2fkcal", dieta.getCalorias()));
                txtCarboidratos.setText(String.format(Food4fitApp.LOCALE, "%.2fg carb", dieta.getCarboidratos()));
                txtGorduras.setText(String.format(Food4fitApp.LOCALE, "%.2fg gord", dieta.getGorduras()));
                txtData.setText(new SimpleDateFormat("dd/MM/yyyy", Food4fitApp.LOCALE).format(new Date()));


                adapter.setListenerAtualizarProgresso(refeicao -> {
                    HistoricoAlimentacao entry = new HistoricoAlimentacao();
                    entry.setData(new Date());
                    entry.setIdRefeicao(refeicao.getData().getId());
                    AppDatabase.getDatabase(getContext()).getHistoricoAlimentacaoDAO().insert(entry);
                });
            }
        }

        return view;
    }
}