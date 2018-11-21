package br.com.food4fit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.food4fit.adapter.DietaAdapter;
import br.com.food4fit.adapter.ItemClickSupport;
import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Dieta;
import br.com.food4fit.model.Usuario;

public class DietasFragment extends Fragment {
    private final List<Dieta> dietas = new ArrayList<>();
    private DietaAdapter adapter;
    private Usuario usuario;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dietas, container, false);
        if (getActivity() != null && getContext() != null) {
            usuario = ((Food4fitApp) getActivity().getApplication()).getUsuario();

            RecyclerView rvDietas = view.findViewById(R.id.rv_dietas);
            rvDietas.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            rvDietas.setLayoutManager(new LinearLayoutManager(getContext()));

            adapter = new DietaAdapter(getContext(), dietas);
            ItemClickSupport.addTo(rvDietas)
                    .setOnItemClickListener((recyclerView, position, v) -> {
                        Intent intent = new Intent(getActivity(), DietaActivity.class);
                        intent.putExtra("dieta", dietas.get(position));
                        startActivity(intent);
                    })
                    .setOnItemLongClickListener((recyclerView, position, v) -> {
                        Dieta dieta = dietas.get(position);
                        DietaDialogFragment dialog = new DietaDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("dieta", dieta);
                        dialog.setArguments(bundle);
                        dialog.setListenerExcluir(txtView -> {
                            dialog.dismiss();
                            excluirDieta(dieta);
                        });

                        dialog.setListenerEditar(txtView -> {
                            dialog.dismiss();
                            editarDieta(dieta);
                        });

                        dialog.setListenerAtivar(txtView -> {
                            dialog.dismiss();
                            ativarDieta(dieta);
                        });

                        if (getFragmentManager() != null) dialog.show(getFragmentManager(), "dialog");
                        return false;
                    });

            rvDietas.setAdapter(adapter);

            FloatingActionButton fabCadastrarDieta = view.findViewById(R.id.fab_cadastrar_dieta);
            fabCadastrarDieta.setOnClickListener(viewFab -> startActivity(new Intent(getContext(), CadastrarDietaActivity.class)));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dietas.clear();
        dietas.addAll(AppDatabase.getDatabase(getContext()).getDietaDAO().selectAll(usuario.getId()));
        adapter.notifyDataSetChanged();
    }

    private void excluirDieta(Dieta dieta) {
        if (getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),
                    Food4fitApp.isDarkMode(getContext()) ? R.style.Theme_AppCompat_Dialog_Alert : R.style.Theme_AppCompat_Light_Dialog_Alert);
            builder.setTitle("Excluir");
            builder.setMessage("Tem certeza que deseja excluir esta dieta e todas as suas refeições?");
            builder.setPositiveButton("Sim", (dialogInterface, i) -> {
                adapter.notifyItemRemoved(dietas.indexOf(dieta));
                dietas.remove(dieta);
                AppDatabase.getDatabase(getContext()).getDietaDAO().delete(dieta.getData());
            });

            builder.setNegativeButton("Não", null);
            builder.create().show();
        }
    }

    private void editarDieta(Dieta dieta) {
        Intent intent = new Intent(getContext(), CadastrarDietaActivity.class);
        intent.putExtra("dieta", dieta);
        startActivity(intent);
    }

    private void ativarDieta(Dieta dieta) {
        if (getContext() != null && getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),
                    Food4fitApp.isDarkMode(getContext()) ? R.style.Theme_AppCompat_Dialog_Alert : R.style.Theme_AppCompat_Light_Dialog_Alert);
            builder.setTitle("Ativar dieta");
            builder.setMessage("Tem certeza que deseja tornar esta dieta ativa e passar a segui-la diariamente?");
            builder.setPositiveButton("Sim", (dialogInterface, i) -> {
                dieta.getData().setAtiva(true);
                AppDatabase.getDatabase(getContext()).getDietaDAO().desativarDietas(usuario.getId());
                AppDatabase.getDatabase(getContext()).getDietaDAO().update(dieta.getData());
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Você agora está seguindo a dieta '" + dieta.getData().getTitulo() + "'.", Snackbar.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("Não", null);
            builder.create().show();
        }
    }
}