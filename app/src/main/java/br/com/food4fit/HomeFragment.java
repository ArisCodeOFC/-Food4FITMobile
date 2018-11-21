package br.com.food4fit;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.food4fit.adapter.DietaAtivaRefeicaoAdapter;
import br.com.food4fit.broadcast.SetAlarmReceiver;
import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Dieta;
import br.com.food4fit.model.HistoricoAlimentacao;
import br.com.food4fit.model.ItemAcompanhamento;
import br.com.food4fit.model.Refeicao;
import br.com.food4fit.model.Usuario;

public class HomeFragment extends Fragment {
    private SparseArray<HistoricoAlimentacao> historico;
    private ProgressBar progressBarDieta;
    private TextView txtPorcentagem, txtMeta;
    private Usuario usuario;
    private Dieta dieta;
    private List<Refeicao> refeicoes;

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
            progressBarDieta = view.findViewById(R.id.progress_bar_dieta);
            txtPorcentagem = view.findViewById(R.id.txt_porcentagem_dieta_ativa);
            txtMeta = view.findViewById(R.id.txt_dieta_ativa_meta);

            rvDietaAtiva.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            rvDietaAtiva.setLayoutManager(new LinearLayoutManager(getContext()));

            usuario = ((Food4fitApp) getActivity().getApplication()).getUsuario();
            dieta = AppDatabase.getDatabase(getContext()).getDietaDAO().getDietaAtiva(usuario.getId());
            SetAlarmReceiver.setAlarms(usuario, getContext());
            if (dieta == null) {
                txtSemDietaAtiva.setVisibility(View.VISIBLE);
                layoutDietaAtiva.setVisibility(View.GONE);
            } else {
                txtSemDietaAtiva.setVisibility(View.GONE);
                layoutDietaAtiva.setVisibility(View.VISIBLE);

                historico = getHistorico();
                refeicoes = new ArrayList<>(dieta.getRefeicoes());
                Collections.sort(refeicoes, (refeicao1, refeicao2) -> refeicao1.getData().getHorario().compareTo(refeicao2.getData().getHorario()));
                atualizarProgresso(false);

                DietaAtivaRefeicaoAdapter adapter = new DietaAtivaRefeicaoAdapter(getContext(), refeicoes, historico);
                rvDietaAtiva.setAdapter(adapter);

                txtCalorias.setText(String.format(Food4fitApp.LOCALE, "%.2fkcal", dieta.getCalorias()));
                txtCarboidratos.setText(String.format(Food4fitApp.LOCALE, "%.2fg carb", dieta.getCarboidratos()));
                txtGorduras.setText(String.format(Food4fitApp.LOCALE, "%.2fg gord", dieta.getGorduras()));
                txtData.setText(new SimpleDateFormat("dd/MM/yyyy", Food4fitApp.LOCALE).format(new Date()));

                adapter.setListenerAtualizarProgresso((refeicao, ativo) -> {
                    if (ativo) {
                        HistoricoAlimentacao entry = new HistoricoAlimentacao();
                        entry.setData(new Date());
                        entry.setIdRefeicao(refeicao.getData().getId());
                        AppDatabase.getDatabase(getContext()).getHistoricoAlimentacaoDAO().insert(entry);
                        historico.put(refeicao.getData().getId(), entry);
                    } else {
                        historico.remove(refeicao.getData().getId());
                        AppDatabase.getDatabase(getContext()).getHistoricoAlimentacaoDAO().remove(refeicao.getData().getId());
                    }

                    ItemAcompanhamento acompanhamento = AppDatabase.getDatabase(getContext()).getAcompanhamentoDAO().selecionarDia(usuario.getId());
                    if (acompanhamento == null) {
                        acompanhamento = new ItemAcompanhamento();
                        acompanhamento.setData(new Date());
                        acompanhamento.setCalorias(ativo ? refeicao.getCalorias() : 0);
                        acompanhamento.setIdUsuario(usuario.getId());
                        AppDatabase.getDatabase(getContext()).getAcompanhamentoDAO().insert(acompanhamento);
                    } else {
                        acompanhamento.setCalorias(acompanhamento.getCalorias() + (refeicao.getCalorias() * (ativo ? 1 : -1)));
                        AppDatabase.getDatabase(getContext()).getAcompanhamentoDAO().update(acompanhamento);
                    }

                    atualizarAcompanhamento(acompanhamento);
                    atualizarProgresso(true);
                });

                atualizarAcompanhamento(null);
            }
        }

        return view;
    }

    private SparseArray<HistoricoAlimentacao> getHistorico() {
        List<HistoricoAlimentacao> itensHistorico = AppDatabase.getDatabase(getContext()).getHistoricoAlimentacaoDAO().getHistoricoDia();
        SparseArray<HistoricoAlimentacao> historico = new SparseArray<>();
        for (HistoricoAlimentacao entry : itensHistorico) {
            historico.put(entry.getIdRefeicao(), entry);
        }

        return historico;
    }

    private void atualizarProgresso(boolean animacao) {
        int progresso = 0;
        for (Refeicao refeicao : refeicoes) {
            if (historico.indexOfKey(refeicao.getData().getId()) >= 0) {
                progresso += 1;
            }
        }

        progresso = progresso * 100 / Math.max(refeicoes.size(), 1);
        txtPorcentagem.setText(String.format(Food4fitApp.LOCALE, "%d%%", progresso));

        if (animacao) {
            ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBarDieta, "progress", progressBarDieta.getProgress(), progresso);
            progressAnimator.setDuration(1000);
            progressAnimator.start();
        } else {
            progressBarDieta.setProgress(progresso);
        }
    }

    private void atualizarAcompanhamento(ItemAcompanhamento acompanhamento) {
        if (acompanhamento == null) {
            acompanhamento = AppDatabase.getDatabase(getContext()).getAcompanhamentoDAO().selecionarDia(usuario.getId());
        }

        if (acompanhamento == null) {
            txtMeta.setText(String.format(Food4fitApp.LOCALE, "Você ainda não atingiu sua meta de %.2fkcal hoje. Foram consumidos 0kcal.", dieta.getData().getMeta()));
        } else if (acompanhamento.getCalorias() >= dieta.getData().getMeta()) {
            txtMeta.setText(String.format(Food4fitApp.LOCALE, "Você atingiu sua meta de %.2fkcal hoje. Foram consumidos %.2fkcal.", dieta.getData().getMeta(), acompanhamento.getCalorias()));
        } else {
            txtMeta.setText(String.format(Food4fitApp.LOCALE, "Você ainda não atingiu sua meta de %.2fkcal hoje. Foram consumidos %.2fkcal.", dieta.getData().getMeta(), acompanhamento.getCalorias()));
        }
    }
}