package br.com.food4fit.food4fit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType("FOOD4FIT");
        Log.d("TESTANDO", Arrays.toString(accounts));
        if (accounts.length > 0) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        Button btnEntrar = findViewById(R.id.btn_login_entrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount("chico@picadinho.com", "1234", "AAA");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    public void createAccount(String email, String password, String authToken) {
        Account account = new Account(email, "FOOD4FIT");
        AccountManager am = AccountManager.get(this);
        am.addAccountExplicitly(account, password, null);
        am.setAuthToken(account, "full_access", authToken);
    }
}
