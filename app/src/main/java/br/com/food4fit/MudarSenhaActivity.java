package br.com.food4fit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.food4fit.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MudarSenhaActivity extends AppCompatActivity {
    private EditText edtSenha, edtConfirmacao;
    private String email, codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudar_senha);
        edtSenha = findViewById(R.id.edt_mudar_senha);
        edtConfirmacao = findViewById(R.id.edt_mudar_senha_confirmacao);
        Button btnSalvar = findViewById(R.id.btn_mudar_senha);
        btnSalvar.setOnClickListener(view -> salvarSenha());
        email = getIntent().getStringExtra("email");
        codigo = getIntent().getStringExtra("codigo");
    }

    private void salvarSenha() {
        String senha = edtSenha.getText().toString();
        String confirmacao = edtConfirmacao.getText().toString();
        if (senha.isEmpty()) {
            edtSenha.setError("Preencha uma senha");
            edtSenha.requestFocus();
        } else if (!confirmacao.equals(senha)) {
            edtConfirmacao.setError("As senhas não coincidem");
            edtConfirmacao.requestFocus();
        } else {
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null && imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            new RetrofitConfig().getUsuarioService().trocarSenha(email, codigo, senha).enqueue(
                    new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 204) {
                                Toast.makeText(MudarSenhaActivity.this, "Senha atualizada com sucesso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MudarSenhaActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Snackbar.make(findViewById(android.R.id.content), "Não foi possível concluir sua solicitação. Tente novamente", Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            t.printStackTrace();
                            Snackbar.make(findViewById(android.R.id.content), "Não foi possível concluir sua solicitação. Tente novamente", Snackbar.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }
}
