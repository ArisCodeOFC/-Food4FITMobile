package br.com.food4fit.food4fit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.food4fit.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.model.Dieta;
import br.com.food4fit.food4fit.model.DietaEntity;

public class CadastrarDietaActivity extends AppCompatActivity {
    private TextInputEditText edtTitulo, edtDescricao, edtMeta;
    private Dieta dieta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_dieta);
        setFinishOnTouchOutside(false);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        edtTitulo = findViewById(R.id.edt_dieta_titulo);
        edtDescricao = findViewById(R.id.edt_dieta_descricao);
        edtMeta = findViewById(R.id.edt_dieta_meta);

        Intent intent = getIntent();
        if (intent != null) {
            dieta = (Dieta) intent.getSerializableExtra("dieta");
            if (dieta != null) {
                edtTitulo.setText(dieta.getData().getTitulo());
                edtDescricao.setText(dieta.getData().getDescricao());
                edtMeta.setText(String.valueOf(dieta.getData().getMeta()));
            }
        }

        Button btnSalvar = findViewById(R.id.btn_dieta_salvar);
        btnSalvar.setOnClickListener(view -> salvarDieta());
    }

    private void salvarDieta() {
        String titulo = edtTitulo.getText().toString();
        String descricao = edtDescricao.getText().toString();
        String meta = edtMeta.getText().toString();

        if (titulo.isEmpty()) {
            edtTitulo.setError("Preencha um título");
            edtTitulo.requestFocus();
        } else if (descricao.isEmpty()) {
            edtDescricao.setError("Preencha uma descrição");
            edtDescricao.requestFocus();
        } else if (meta.isEmpty()) {
            edtMeta.setError("Preencha uma meta");
            edtMeta.requestFocus();
        } else {
            DietaEntity dieta = new DietaEntity();
            dieta.setTitulo(titulo);
            dieta.setDescricao(descricao);
            dieta.setMeta(Double.parseDouble(meta));
            dieta.setIdUsuario(((Food4fitApp) getApplication()).getUsuario().getId());
            if (this.dieta != null) {
                dieta.setId(this.dieta.getData().getId());
                AppDatabase.getDatabase(this).getDietaDAO().update(dieta);
            } else {
                AppDatabase.getDatabase(this).getDietaDAO().insert(dieta);
            }

            finish();
        }
    }
}