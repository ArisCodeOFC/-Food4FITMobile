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
import br.com.food4fit.model.Refeicao;

public class RefeicaoDialogFragment extends BottomSheetDialogFragment {
    private Refeicao refeicao;
    private View.OnClickListener listenerExcluir;
    private View.OnClickListener listenerEditar;

    @Override
    public void setArguments(Bundle args) {
        refeicao = (Refeicao) args.getSerializable("refeicao");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_refeicao, container, false);
        TextView txtTitulo = view.findViewById(R.id.txt_dialog_refeicao_titulo);
        TextView txtExcluir = view.findViewById(R.id.txt_dialog_refeicao_excluir);
        TextView txtEditar = view.findViewById(R.id.txt_dialog_refeicao_editar);
        txtTitulo.setText(refeicao.getData().getTitulo());
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
