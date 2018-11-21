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
import br.com.food4fit.model.Dieta;

public class DietaDialogFragment extends BottomSheetDialogFragment {
    private Dieta dieta;
    private View.OnClickListener listenerExcluir;
    private View.OnClickListener listenerEditar;
    private View.OnClickListener listenerAtivar;

    @Override
    public void setArguments(Bundle args) {
        dieta = (Dieta) args.getSerializable("dieta");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_dieta, container, false);
        TextView txtTitulo = view.findViewById(R.id.txt_dialog_dieta_titulo);
        TextView txtExcluir = view.findViewById(R.id.txt_dialog_dieta_excluir);
        TextView txtEditar = view.findViewById(R.id.txt_dialog_dieta_editar);
        TextView txtAtivar = view.findViewById(R.id.txt_dialog_dieta_tornar_ativa);
        txtTitulo.setText(dieta.getData().getTitulo());
        txtExcluir.setOnClickListener(listenerExcluir);
        txtEditar.setOnClickListener(listenerEditar);
        txtAtivar.setOnClickListener(listenerAtivar);
        return view;
    }

    public void setListenerExcluir(View.OnClickListener listenerExcluir) {
        this.listenerExcluir = listenerExcluir;
    }

    public void setListenerEditar(View.OnClickListener listenerEditar) {
        this.listenerEditar = listenerEditar;
    }

    public void setListenerAtivar(View.OnClickListener listenerAtivar) {
        this.listenerAtivar = listenerAtivar;
    }
}