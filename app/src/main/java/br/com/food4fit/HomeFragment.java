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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.food4fit.adapter.DietaAtivaRefeicaoAdapter;
import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Dieta;
import br.com.food4fit.model.Refeicao;
import br.com.food4fit.model.Usuario;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity activity = ((MainActivity) getActivity());
        if (activity != null) {
            Toolbar toolbar = view.findViewById(R.id.toolbar);
            activity.configureToolbar(toolbar);
            activity.setTitle("Minha dieta");
        }

        TextView txtSemDietaAtiva = view.findViewById(R.id.txt_sem_dieta_ativa);
        RecyclerView rvDietaAtiva = view.findViewById(R.id.rv_dieta_ativa);
        LinearLayout layoutDietaAtiva = view.findViewById(R.id.layout_dieta_ativa);
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
            DietaAtivaRefeicaoAdapter adapter = new DietaAtivaRefeicaoAdapter(getContext(), refeicoes);
            rvDietaAtiva.setAdapter(adapter);
        }

        return view;
    }
}