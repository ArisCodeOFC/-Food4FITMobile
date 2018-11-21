package br.com.food4fit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.ItemDadosSaude;
import br.com.food4fit.model.Usuario;

public class DadosSaudeActivity extends AppCompatActivity {
    private Usuario usuario;
    private EditText edtPeso, edtAltura;
    private TextView txtImc, txtConselho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Food4fitApp.isDarkMode(this)) {
            setTheme(R.style.Theme_AppCompat_Dialog_Dark);
        }

        setContentView(R.layout.activity_dados_saude);
        setFinishOnTouchOutside(false);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        usuario = ((Food4fitApp) getApplication()).getUsuario();
        edtPeso = findViewById(R.id.edt_dados_saude_peso);
        edtAltura = findViewById(R.id.edt_dados_saude_altura);
        txtImc = findViewById(R.id.txt_dados_saude_imc);
        txtConselho = findViewById(R.id.txt_dados_saude_mensagem);
        Button btnSalvar = findViewById(R.id.btn_dados_saude_salvar);

        edtPeso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                atualizarImc();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edtAltura.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                atualizarImc();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSalvar.setOnClickListener((view) -> salvarDados());

        ItemDadosSaude dados = AppDatabase.getDatabase(this).getDadosSaudeDAO().selecionarAtual(usuario.getId());
        if (dados != null) {
            edtPeso.setText(String.valueOf(dados.getPeso()));
            edtAltura.setText(String.valueOf(dados.getAltura()));
            atualizarImc();
        }
    }

    private void atualizarImc() {
        double peso = Double.parseDouble(edtPeso.getText().toString().isEmpty() ? "0" : edtPeso.getText().toString());
        double altura = Double.parseDouble(edtAltura.getText().toString().isEmpty() ? "0" : edtAltura.getText().toString());
        if (peso != 0 && altura != 0) {
            double imc = peso / (altura * altura);
            txtImc.setText(String.format(Food4fitApp.LOCALE, "%.2f", imc));
            if (imc < 17) {
                txtConselho.setText(R.string.conselho_imc_1);
            } else if (imc < 18.5) {
                txtConselho.setText(R.string.conselho_imc_2);
            } else if (imc < 25) {
                txtConselho.setText(R.string.conselho_imc_3);
            } else if (imc < 30) {
                txtConselho.setText(R.string.conselho_imc_4);
            } else if (imc < 35) {
                txtConselho.setText(R.string.conselho_imc_5);
            } else if (imc < 40) {
                txtConselho.setText(R.string.conselho_imc_6);
            } else {
                txtConselho.setText(R.string.conselho_imc_7);
            }
        }
    }

    private void salvarDados() {
        if (edtPeso.getText().toString().isEmpty() || edtPeso.getText().toString().equals("0")) {
            edtPeso.setError("Preencha um peso válido");
            edtPeso.requestFocus();
        } else if (edtAltura.getText().toString().isEmpty() || edtAltura.getText().toString().equals("0")) {
            edtAltura.setError("Preencha uma altura válida");
            edtAltura.requestFocus();
        } else {
            ItemDadosSaude dados = new ItemDadosSaude();
            dados.setPeso(Double.parseDouble(edtPeso.getText().toString()));
            dados.setAltura(Double.parseDouble(edtAltura.getText().toString()));
            dados.setIdUsuario(usuario.getId());
            dados.setData(new Date());
            AppDatabase.getDatabase(this).getDadosSaudeDAO().insert(dados);
            finish();
        }
    }
}
