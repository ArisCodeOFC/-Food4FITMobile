package br.com.food4fit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import br.com.food4fit.adapter.ItemClickSupport;
import br.com.food4fit.adapter.RefeicaoAdapter;
import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Dieta;
import br.com.food4fit.model.Refeicao;
import br.com.food4fit.model.Usuario;

public class DietaActivity extends AppCompatActivity {
    private Dieta dieta;
    private RefeicaoAdapter adapter;
    private List<Refeicao> refeicoes = new ArrayList<>();
    private TextView txtCalorias, txtCarboidratos, txtGorduras, txtProteinas;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        usuario = ((Food4fitApp) getApplication()).getUsuario();

        Intent intent = getIntent();
        if (intent != null) {
            dieta = (Dieta) intent.getSerializableExtra("dieta");
        }

        RecyclerView rvRefeicoes = findViewById(R.id.rv_refeicoes);
        rvRefeicoes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvRefeicoes.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RefeicaoAdapter(this, refeicoes);
        ItemClickSupport.addTo(rvRefeicoes)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Intent intentRefeicao = new Intent(DietaActivity.this, RefeicaoActivity.class);
                    intentRefeicao.putExtra("dieta", dieta);
                    intentRefeicao.putExtra("refeicao", refeicoes.get(position));
                    startActivity(intentRefeicao);
                })
                .setOnItemLongClickListener((recyclerView, position, v) -> {
                    Refeicao refeicao = refeicoes.get(position);
                    RefeicaoDialogFragment dialog = new RefeicaoDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("refeicao", refeicao);
                    dialog.setArguments(bundle);
                    dialog.setListenerExcluir(view -> {
                        dialog.dismiss();
                        excluirRefeicao(refeicao);
                    });

                    dialog.setListenerEditar(view -> {
                        dialog.dismiss();
                        editarRefeicao(refeicao);
                    });

                    dialog.show(getSupportFragmentManager(), "dialog");
                    return false;
                });

        rvRefeicoes.setAdapter(adapter);

        FloatingActionButton fabCadastrarRefeicao = findViewById(R.id.fab_cadastrar_refeicao);
        fabCadastrarRefeicao.setOnClickListener(view -> {
            Intent intentCadastrar = new Intent(DietaActivity.this, CadastrarRefeicaoActivity.class);
            intentCadastrar.putExtra("dieta", dieta);
            startActivity(intentCadastrar);
        });

        txtCalorias = findViewById(R.id.txt_calorias_visualizar_dieta);
        txtCarboidratos = findViewById(R.id.txt_carboidratos_visualizar_dieta);
        txtGorduras = findViewById(R.id.txt_gorduras_visualizar_dieta);
        txtProteinas = findViewById(R.id.txt_proteinas_visualizar_dieta);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dieta = AppDatabase.getDatabase(this).getDietaDAO().select(dieta.getData().getId());
        setTitle(dieta.getData().getTitulo());

        refeicoes.clear();
        refeicoes.addAll(dieta.getRefeicoes());
        Collections.sort(refeicoes, (refeicao1, refeicao2) -> refeicao1.getData().getHorario().compareTo(refeicao2.getData().getHorario()));

        adapter.notifyDataSetChanged();
        atualizarDados();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dieta, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_dieta_excluir) {
            excluirDieta();
        } else if (id == R.id.item_dieta_editar) {
            editarDieta();
        } else if (id == R.id.item_dieta_tornar_ativa) {
            ativarDieta();
        }

        return super.onOptionsItemSelected(item);
    }

    private void atualizarDados() {
        dieta.setDadosAtualizados(false);
        txtCalorias.setText(String.format(new Locale("pt","BR"), "%.2fkcal", dieta.getCalorias()));
        txtCarboidratos.setText(String.format(new Locale("pt","BR"), "%.2fg carb", dieta.getCarboidratos()));
        txtGorduras.setText(String.format(new Locale("pt","BR"), "%.2fg gord", dieta.getGorduras()));
        txtProteinas.setText(String.format(new Locale("pt","BR"), "%.2fg prot", dieta.getProteinas()));
    }

    private void excluirRefeicao(Refeicao refeicao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir");
        builder.setMessage("Tem certeza que deseja excluir esta refeição e todos os seus alimentos?");
        builder.setPositiveButton("Sim", (dialogInterface, i) -> {
            adapter.notifyItemRemoved(refeicoes.indexOf(refeicao));
            refeicoes.remove(refeicao);
            dieta.getRefeicoes().remove(refeicao);
            AppDatabase.getDatabase(this).getRefeicaoDAO().delete(refeicao.getData());
            atualizarDados();
        });

        builder.setNegativeButton("Não", null);
        builder.create().show();
    }

    private void editarRefeicao(Refeicao refeicao) {
        Intent intent = new Intent(DietaActivity.this, CadastrarRefeicaoActivity.class);
        intent.putExtra("dieta", dieta);
        intent.putExtra("refeicao", refeicao);
        startActivity(intent);
    }

    private void excluirDieta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir");
        builder.setMessage("Tem certeza que deseja excluir esta dieta e todas as suas refeições?");
        builder.setPositiveButton("Sim", (dialogInterface, i) -> {
            AppDatabase.getDatabase(this).getDietaDAO().delete(dieta.getData());
            finish();
        });

        builder.setNegativeButton("Não", null);
        builder.create().show();
    }

    private void editarDieta() {
        Intent intent = new Intent(this, CadastrarDietaActivity.class);
        intent.putExtra("dieta", dieta);
        startActivity(intent);
    }

    private void ativarDieta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ativar dieta");
        builder.setMessage("Tem certeza que deseja tornar esta dieta ativa e passar a segui-la diariamente?");
        builder.setPositiveButton("Sim", (dialogInterface, i) -> {
            dieta.getData().setAtiva(true);
            AppDatabase.getDatabase(this).getDietaDAO().desativarDietas(usuario.getId());
            AppDatabase.getDatabase(this).getDietaDAO().update(dieta.getData());
            Snackbar.make(findViewById(android.R.id.content), "Você agora está seguindo a dieta '" + dieta.getData().getTitulo() + "'.", Snackbar.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Não", null);
        builder.create().show();
    }
}