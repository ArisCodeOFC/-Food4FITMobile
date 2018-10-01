package br.com.food4fit.food4fit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
            startActivity(new Intent(this, MainActivity.class));
            finish();
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
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
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
            setAccount(email, senha, "AAA");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}
