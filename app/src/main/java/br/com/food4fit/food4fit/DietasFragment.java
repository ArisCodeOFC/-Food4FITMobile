package br.com.food4fit.food4fit;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import br.com.food4fit.food4fit.adapter.DietaAdapter;
import br.com.food4fit.food4fit.model.Dieta;


public class DietasFragment extends BottomSheetDialogFragment {
    public DietasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dietas, container, false);

        List<Dieta> dietas = new ArrayList<>();
        dietas.add(new Dieta(1, "Dieta da Lua"));
        dietas.add(new Dieta(2, "Dieta da Terra"));
        dietas.add(new Dieta(3, "Dieta do Sol"));
        dietas.add(new Dieta(4, "Dieta de Verão"));
        dietas.add(new Dieta(5, "Dieta de Inverno"));

        RecyclerView rvDietas = view.findViewById(R.id.rv_dietas);
        rvDietas.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvDietas.setLayoutManager(new LinearLayoutManager(getContext()));

        DietaAdapter adapter = new DietaAdapter(getContext(), dietas, new DietaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Dieta item) {
                DietaDialogFragment dialog = new DietaDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("dieta", item);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        rvDietas.setAdapter(adapter);

        return view;
    }
}