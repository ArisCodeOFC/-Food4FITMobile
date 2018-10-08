package br.com.food4fit.food4fit;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

import br.com.food4fit.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.model.Dieta;
import br.com.food4fit.food4fit.model.RefeicaoEntity;

public class CadastrarRefeicaoActivity extends AppCompatActivity {
    private TextInputEditText edtTitulo;
    private TextInputEditText edtDescricao;
    private TextInputEditText edtHorario;
    private Dieta dieta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_refeicao);
        setFinishOnTouchOutside(false);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Intent intent = getIntent();
        if (intent != null) {
            dieta = (Dieta) intent.getSerializableExtra("dieta");
        }

        edtTitulo = findViewById(R.id.edt_refeicao_titulo);
        edtDescricao = findViewById(R.id.edt_refeicao_descricao);
        edtHorario = findViewById(R.id.edt_refeicao_horario);
        edtHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePicker = new TimePickerDialog(CadastrarRefeicaoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edtHorario.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                timePicker.setTitle("Selecione um horário");
                timePicker.show();
            }
        });

        Button btnSalvar = findViewById(R.id.btn_refeicao_salvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarRefeicao();
            }
        });
    }

    private void salvarRefeicao() {
        String titulo = edtTitulo.getText().toString();
        String descricao = edtDescricao.getText().toString();
        String horario = edtHorario.getText().toString();

        if (titulo.isEmpty()) {
            edtTitulo.setError("Preencha um título");
            edtTitulo.requestFocus();
        } else if (descricao.isEmpty()) {
            edtDescricao.setError("Preencha uma descrição");
            edtDescricao.requestFocus();
        } else if (horario.isEmpty()) {
            edtHorario.setError("Preencha um horário");
            edtHorario.requestFocus();
        } else {
            RefeicaoEntity refeicao = new RefeicaoEntity();
            refeicao.setTitulo(titulo);
            refeicao.setDescricao(descricao);
            refeicao.setHorario(horario);
            refeicao.setIdDieta(dieta.getDieta().getId());
            dieta.getRefeicoes().add(refeicao);
            AppDatabase.getDatabase(this).getRefeicaoDAO().insert(refeicao);
            finish();
        }
    }
}
