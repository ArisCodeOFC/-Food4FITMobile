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

import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.food4fit.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarSenhaActivity extends AppCompatActivity {
    private EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        Button btnEnviar = findViewById(R.id.btn_recuperar_senha_enviar);
        edtEmail = findViewById(R.id.edt_recuperar_senha_email);

        btnEnviar.setOnClickListener((view) -> this.enviarEmail());
    }

    private void enviarEmail() {
        String email = edtEmail.getText().toString();
        if (email.isEmpty()) {
            edtEmail.setError("Preencha um email");
            edtEmail.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Este email não é válido");
            edtEmail.requestFocus();
        } else {
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null && imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            new RetrofitConfig().getUsuarioService().enviarEmailRecuperacao(email).enqueue(
                    new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 404) {
                                Snackbar.make(findViewById(android.R.id.content), "Email não encontrado", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(RecuperarSenhaActivity.this, VerificarCodigoActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
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
