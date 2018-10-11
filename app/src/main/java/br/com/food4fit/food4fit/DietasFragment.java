package br.com.food4fit.food4fit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.food4fit.food4fit.adapter.DietaAdapter;
import br.com.food4fit.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.config.ApplicationData;
import br.com.food4fit.food4fit.model.Dieta;
import br.com.food4fit.food4fit.model.Usuario;

public class DietasFragment extends Fragment {
    private List<Dieta> dietas = new ArrayList<>();
    private DietaAdapter adapter;
    private Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dietas, container, false);

        usuario = ((ApplicationData) getActivity().getApplication()).getUsuario();

        RecyclerView rvDietas = view.findViewById(R.id.rv_dietas);
        rvDietas.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvDietas.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DietaAdapter(getContext(), dietas, item -> {
            DietaDialogFragment dialog = new DietaDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("dieta", item);
            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(), "dialog");
        });

        rvDietas.setAdapter(adapter);

        FloatingActionButton fabCadastrarDieta = view.findViewById(R.id.fab_cadastrar_dieta);
        fabCadastrarDieta.setOnClickListener(view1 -> startActivity(new Intent(getContext(), CadastrarDietaActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dietas.clear();
        dietas.addAll(AppDatabase.getDatabase(getContext()).getDietaDAO().selectAll(usuario.getId()));
        adapter.notifyDataSetChanged();
    }
}