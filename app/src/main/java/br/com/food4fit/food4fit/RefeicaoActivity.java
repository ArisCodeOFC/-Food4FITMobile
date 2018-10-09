package br.com.food4fit.food4fit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import br.com.food4fit.food4fit.adapter.AlimentoAdapter;
import br.com.food4fit.food4fit.model.Alimento;
import br.com.food4fit.food4fit.model.Refeicao;

public class RefeicaoActivity extends AppCompatActivity {
    private Refeicao refeicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicao);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            refeicao = (Refeicao) intent.getSerializableExtra("refeicao");
            setTitle(refeicao.getRefeicao().getTitulo());
        }

        RecyclerView rvAlimentos = findViewById(R.id.rv_alimentos);
        rvAlimentos.setLayoutManager(new GridLayoutManager(this, 2));

        List<Alimento> alimentos = refeicao.getAlimentos();
        Alimento a = new Alimento();
        a.setId(1);
        a.setTitulo("kkk");
        alimentos.add(a);
        alimentos.add(a);
        alimentos.add(a);
        alimentos.add(a);
        alimentos.add(a);

        AlimentoAdapter adapter = new AlimentoAdapter(this, alimentos, new AlimentoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Alimento item) {

            }
        });

        rvAlimentos.setAdapter(adapter);

        FloatingActionButton fabCadastrarAlimento = findViewById(R.id.fab_cadastrar_alimento);
        fabCadastrarAlimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RefeicaoActivity.this, CadastrarAlimentoActivity.class);
                intent.putExtra("refeicao", refeicao);
                startActivity(intent);
            }
        });
    }
}