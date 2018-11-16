package br.com.food4fit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Alimento;

public class AlimentoDialogFragment extends BottomSheetDialogFragment {
    private Alimento alimento;
    private View.OnClickListener listenerExcluir;
    private View.OnClickListener listenerEditar;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setArguments(Bundle args) {
        alimento = (Alimento) args.getSerializable("alimento");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_alimento, container, false);
        TextView txtTitulo = view.findViewById(R.id.txt_dialog_alimento_titulo);
        TextView txtExcluir = view.findViewById(R.id.txt_dialog_alimento_excluir);
        TextView txtEditar = view.findViewById(R.id.txt_dialog_alimento_editar);
        txtTitulo.setText(alimento.getTitulo());
        txtExcluir.setOnClickListener(listenerExcluir);
        txtEditar.setOnClickListener(listenerEditar);
        return view;
    }

    public void setListenerExcluir(View.OnClickListener listenerExcluir) {
        this.listenerExcluir = listenerExcluir;
    }

    public void setListenerEditar(View.OnClickListener listenerEditar) {
        this.listenerEditar = listenerEditar;
    }
}
