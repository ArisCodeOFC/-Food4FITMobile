package br.com.food4fit.food4fit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.food4fit.food4fit.model.Dieta;

public class DietaDialogFragment extends BottomSheetDialogFragment {
    private Dieta dieta;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setArguments(Bundle args) {
        dieta = (Dieta) args.getSerializable("dieta");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_dieta, container, false);
        TextView txtTitulo = view.findViewById(R.id.txt_dialog_dieta_titulo);
        txtTitulo.setText(dieta.getTitulo());
        return view;
    }
}