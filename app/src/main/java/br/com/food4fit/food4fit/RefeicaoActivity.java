package br.com.food4fit.food4fit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.food4fit.food4fit.adapter.AlimentoAdapter;
import br.com.food4fit.food4fit.adapter.ItemClickSupport;
import br.com.food4fit.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.model.Alimento;
import br.com.food4fit.food4fit.model.Refeicao;

public class RefeicaoActivity extends AppCompatActivity {
    private Refeicao refeicao;
    private List<Alimento> alimentos = new ArrayList<>();
    private AlimentoAdapter adapter;
    private TextView txtCalorias, txtCarboidratos, txtGorduras, txtProteinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            refeicao = (Refeicao) intent.getSerializableExtra("refeicao");
            setTitle(refeicao.getData().getTitulo());
        }

        RecyclerView rvAlimentos = findViewById(R.id.rv_alimentos);
        rvAlimentos.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new AlimentoAdapter(this, alimentos);

        rvAlimentos.setAdapter(adapter);
        ItemClickSupport.addTo(rvAlimentos)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Alimento alimento = alimentos.get(position);
                    AlimentoDialogFragment dialog = new AlimentoDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("alimento", alimento);
                    dialog.setArguments(bundle);
                    dialog.setListenerExcluir(view -> {
                        dialog.dismiss();
                        excluirAlimento(alimento);
                    });

                    dialog.setListenerEditar(view -> {
                        dialog.dismiss();
                        editarAlimento(alimento);
                    });

                    dialog.show(getSupportFragmentManager(), "dialog");
                });

        FloatingActionButton fabCadastrarAlimento = findViewById(R.id.fab_cadastrar_alimento);
        fabCadastrarAlimento.setOnClickListener(view -> {
            Intent intentAlimento = new Intent(RefeicaoActivity.this, CadastrarAlimentoActivity.class);
            intentAlimento.putExtra("refeicao", refeicao);
            startActivity(intentAlimento);
        });

        txtCalorias = findViewById(R.id.txt_calorias_visualizar_refeicao);
        txtCarboidratos = findViewById(R.id.txt_carboidratos_visualizar_refeicao);
        txtGorduras = findViewById(R.id.txt_gorduras_visualizar_refeicao);
        txtProteinas = findViewById(R.id.txt_proteinas_visualizar_refeicao);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refeicao = AppDatabase.getDatabase(this).getRefeicaoDAO().select(refeicao.getData().getId());
        alimentos.clear();
        alimentos.addAll(refeicao.getAlimentos());
        adapter.notifyDataSetChanged();
        atualizarDados();
    }

    private void atualizarDados() {
        refeicao.setDadosAtualizados(false);
        txtCalorias.setText(String.format(new Locale("pt","BR"), "%.2fkcal", refeicao.getCalorias()));
        txtCarboidratos.setText(String.format(new Locale("pt","BR"), "%.2fg carb", refeicao.getCarboidratos()));
        txtGorduras.setText(String.format(new Locale("pt","BR"), "%.2fg gord", refeicao.getGorduras()));
        txtProteinas.setText(String.format(new Locale("pt","BR"), "%.2fg prot", refeicao.getProteinas()));
    }

    private void excluirAlimento(Alimento alimento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir");
        builder.setMessage("Tem certeza que deseja excluir este alimento?");
        builder.setPositiveButton("Sim", (dialogInterface, i) -> {
            adapter.notifyItemRemoved(alimentos.indexOf(alimento));
            alimentos.remove(alimento);
            refeicao.getAlimentos().remove(alimento);
            AppDatabase.getDatabase(this).getAlimentoDAO().delete(alimento);
            atualizarDados();
        });

        builder.setNegativeButton("Não", null);
        builder.create().show();
    }

    private void editarAlimento(Alimento alimento) {
        Intent intent = new Intent(RefeicaoActivity.this, CadastrarAlimentoActivity.class);
        intent.putExtra("refeicao", refeicao);
        intent.putExtra("alimento", alimento);
        startActivity(intent);
    }
}