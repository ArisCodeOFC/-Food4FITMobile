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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.food4fit.adapter.HidratacaoAdapter;
import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.ItemHidratacao;
import br.com.food4fit.model.Usuario;

public class HidratacaoFragment extends Fragment {
    private List<ItemHidratacao> itens;
    private TextView txtTotal;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hidratacao, container, false);
        if (getActivity() != null && getContext() != null) {
            Usuario usuario = ((Food4fitApp) getActivity().getApplication()).getUsuario();

            TextView txtData = view.findViewById(R.id.txt_hidratacao_data);
            txtTotal = view.findViewById(R.id.txt_hidratacao_total_consumido);
            RecyclerView rvHidratacao = view.findViewById(R.id.rv_hidratacao);
            EditText edtQuantidade = view.findViewById(R.id.edt_hidratacao_quantidade);
            Button btnRegistrar = view.findViewById(R.id.btn_hidratacao_registrar);

            itens = AppDatabase.getDatabase(getContext()).getHidratacaoDAO().selecionar(usuario.getId());
            Collections.sort(itens, (item1, item2) -> item1.getData().compareTo(item2.getData()));

            rvHidratacao.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            rvHidratacao.setLayoutManager(new LinearLayoutManager(getContext()));
            HidratacaoAdapter adapter = new HidratacaoAdapter(getContext(), itens);
            rvHidratacao.setAdapter(adapter);

            txtData.setText(new SimpleDateFormat("dd/MM/yyyy", Food4fitApp.LOCALE).format(new Date()));
            btnRegistrar.setOnClickListener(v -> {
                if (edtQuantidade.getText().toString().isEmpty()) {
                    edtQuantidade.setError("Preencha uma quantidade");
                    edtQuantidade.requestFocus();
                } else {
                    ItemHidratacao item = new ItemHidratacao();
                    item.setQuantidade(Integer.parseInt(edtQuantidade.getText().toString()));
                    item.setData(new Date());
                    item.setIdUsuario(usuario.getId());
                    AppDatabase.getDatabase(getContext()).getHidratacaoDAO().insert(item);
                    edtQuantidade.setText("");
                    itens.add(item);
                    adapter.notifyDataSetChanged();
                    atualizarTotal();
                }
            });

            atualizarTotal();
        }

        return view;
    }

    private void atualizarTotal() {
        int total = 0;
        for (ItemHidratacao item : itens) {
            total += item.getQuantidade();
        }

        txtTotal.setText(String.format(Food4fitApp.LOCALE, "%dmL", total));
    }
}