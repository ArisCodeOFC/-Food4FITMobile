package br.com.food4fit.food4fit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.food4fit.food4fit.adapter.RefeicaoAdapter;
import br.com.food4fit.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.model.Dieta;
import br.com.food4fit.food4fit.model.Refeicao;

public class DietaActivity extends AppCompatActivity {
    private Dieta dieta;
    private RefeicaoAdapter adapter;
    private List<Refeicao> refeicoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            dieta = (Dieta) intent.getSerializableExtra("dieta");
            setTitle(dieta.getDieta().getTitulo());
        }

        RecyclerView rvRefeicoes = findViewById(R.id.rv_refeicoes);
        rvRefeicoes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvRefeicoes.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RefeicaoAdapter(this, refeicoes, new RefeicaoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Refeicao item) {
                Intent intent = new Intent(DietaActivity.this, RefeicaoActivity.class);
                intent.putExtra("refeicao", item);
                startActivity(intent);
            }
        });

        rvRefeicoes.setAdapter(adapter);

        FloatingActionButton fabCadastrarRefeicao = findViewById(R.id.fab_cadastrar_refeicao);
        fabCadastrarRefeicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DietaActivity.this, CadastrarRefeicaoActivity.class);
                intent.putExtra("dieta", dieta);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dieta = AppDatabase.getDatabase(this).getDietaDAO().select(dieta.getDieta().getId());
        refeicoes.clear();
        refeicoes.addAll(dieta.getRefeicoes());
        adapter.notifyDataSetChanged();
    }
}