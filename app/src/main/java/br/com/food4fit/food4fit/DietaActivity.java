package br.com.food4fit.food4fit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import br.com.food4fit.food4fit.adapter.RefeicaoAdapter;
import br.com.food4fit.food4fit.model.DietaEntity;
import br.com.food4fit.food4fit.model.RefeicaoEntity;

public class DietaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            DietaEntity dieta = (DietaEntity) intent.getSerializableExtra("dieta");
            setTitle(dieta.getTitulo());
        }

        List<RefeicaoEntity> refeicoes = new ArrayList<>();
        refeicoes.add(new RefeicaoEntity(1, "Café da Manhã 1"));
        refeicoes.add(new RefeicaoEntity(1, "Café da Manhã 2"));
        refeicoes.add(new RefeicaoEntity(1, "Café da Manhã 3"));
        refeicoes.add(new RefeicaoEntity(1, "Café da Manhã 4"));
        refeicoes.add(new RefeicaoEntity(1, "Café da Manhã 5"));

        RecyclerView rvRefeicoes = findViewById(R.id.rv_refeicoes);
        rvRefeicoes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvRefeicoes.setLayoutManager(new LinearLayoutManager(this));

        RefeicaoAdapter adapter = new RefeicaoAdapter(this, refeicoes, new RefeicaoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RefeicaoEntity item) {
                
            }
        });

        rvRefeicoes.setAdapter(adapter);
    }
}