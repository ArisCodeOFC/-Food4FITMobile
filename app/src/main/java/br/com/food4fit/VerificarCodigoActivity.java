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

public class VerificarCodigoActivity extends AppCompatActivity {
    private Button btnVerificar;
    private EditText edtCodigo;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_codigo);
        btnVerificar = findViewById(R.id.btn_verificar_codigo);
        edtCodigo = findViewById(R.id.edt_verficar_codigo);
        btnVerificar.setOnClickListener(view -> verificarCodigo());
        email = getIntent().getStringExtra("email");
    }

    private void verificarCodigo() {
        String codigo = edtCodigo.getText().toString();
        if (codigo.isEmpty()) {
            edtCodigo.setError("Preencha um código");
            edtCodigo.requestFocus();
        } else {
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null && imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            btnVerificar.setEnabled(false);
            new RetrofitConfig().getUsuarioService().verificarCodigo(email, codigo).enqueue(
                    new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            btnVerificar.setEnabled(true);
                            if (response.code() == 403) {
                                Snackbar.make(findViewById(android.R.id.content), "Código incorreto", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(VerificarCodigoActivity.this, MudarSenhaActivity.class);
                                intent.putExtra("email", email);
                                intent.putExtra("codigo", codigo);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            btnVerificar.setEnabled(true);
                            t.printStackTrace();
                            Snackbar.make(findViewById(android.R.id.content), "Não foi possível concluir sua solicitação. Tente novamente", Snackbar.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }
}
