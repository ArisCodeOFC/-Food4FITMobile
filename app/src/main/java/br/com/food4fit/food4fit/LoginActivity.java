package br.com.food4fit.food4fit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.com.food4fit.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.config.RetrofitConfig;
import br.com.food4fit.food4fit.model.LoginModel;
import br.com.food4fit.food4fit.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private AccountManager accountManager;
    private TextInputLayout tilEmail;
    private TextInputEditText edtEmail;
    private TextInputLayout tilSenha;
    private TextInputEditText edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType("FOOD4FIT");
        if (accounts.length > 0) {
            Account account = accounts[0];
            Usuario usuario = AppDatabase.getDatabase(LoginActivity.this).getUsuarioDAO().findByEmail(account.name);
            if (usuario != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
            }
        }

        tilEmail = findViewById(R.id.til_login_email);
        edtEmail = findViewById(R.id.edt_login_email);
        tilSenha = findViewById(R.id.til_login_senha);
        edtSenha = findViewById(R.id.edt_login_senha);
    }

    private void setAccount(String email, String password, String authToken) {
        Account account = new Account(email, "FOOD4FIT");
        accountManager.addAccountExplicitly(account, password, null);
        accountManager.setAuthToken(account, "full_access", authToken);
    }

    public void login(View view) {
        final String email = edtEmail.getText().toString();
        final String senha = edtSenha.getText().toString();
        tilEmail.setError("");
        tilSenha.setError("");
        if (email.isEmpty()) {
            tilEmail.setError("Preencha um email");
            edtEmail.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Este email não é válido");
            edtEmail.requestFocus();
        } else if (senha.isEmpty()) {
            tilSenha.setError("Preencha uma senha");
            edtSenha.requestFocus();
        } else {
            LoginModel model = new LoginModel(email, senha);
            Call<Usuario> call = new RetrofitConfig().getUsuarioService().login(model);
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.code() == 401) {
                        tilSenha.setError("Email ou senha incorretos.");
                        edtSenha.setText("");
                    } else {
                        Usuario usuario = response.body();
                        setAccount(usuario.getEmail(), senha, usuario.getHash());
                        AppDatabase.getDatabase(LoginActivity.this).getUsuarioDAO().insert(usuario);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("usuario", usuario);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    tilSenha.setError("Erro na comunicação com o servidor");
                    edtSenha.setText("");
                    t.printStackTrace();
                }
            });
        }
    }
}
