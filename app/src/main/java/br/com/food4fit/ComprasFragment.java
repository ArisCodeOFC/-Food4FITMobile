package br.com.food4fit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import br.com.food4fit.adapter.CompraAdapter;
import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Compra;
import br.com.food4fit.model.Usuario;

public class ComprasFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compras, container, false);
        if (getActivity() != null && getContext() != null) {
            Usuario usuario = ((Food4fitApp) getActivity().getApplication()).getUsuario();
            List<Compra> compras =  AppDatabase.getDatabase(getContext()).getCompraDAO().selecionarTodos(usuario.getId());

            RecyclerView rvCompras = view.findViewById(R.id.rv_compras);
            Collections.sort(compras, (item1, item2) -> item1.getData().compareTo(item2.getData()));

            rvCompras.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            rvCompras.setLayoutManager(new LinearLayoutManager(getContext()));
            CompraAdapter adapter = new CompraAdapter(getContext(), compras);
            rvCompras.setAdapter(adapter);
        }

        return view;
    }
}