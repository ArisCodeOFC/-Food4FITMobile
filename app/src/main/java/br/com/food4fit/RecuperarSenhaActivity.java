package br.com.food4fit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import br.com.food4fit.food4fit.R;

public class RecuperarSenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);
        Button btnEnviar = findViewById(R.id.btn_recuperar_senha_enviar);
        btnEnviar.setOnClickListener((view) -> startActivity(new Intent(this, VerificarCodigoActivity.class)));
    }
}
