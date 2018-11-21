package br.com.food4fit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import br.com.food4fit.food4fit.R;

public class VerificarCodigoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_codigo);
        Button btnVerificar = findViewById(R.id.btn_verificar_codigo);
        btnVerificar.setOnClickListener(view -> startActivity(new Intent(this, MudarSenhaActivity.class)));
    }
}
