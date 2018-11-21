package br.com.food4fit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.ItemDadosSaude;
import br.com.food4fit.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilFragment extends Fragment {
    private Food4fitApp application;
    private Usuario usuario;
    private EditText edtNome, edtTelefone, edtCelular;
    private RadioButton rbMasculino, rbFeminino;
    private TextView txtNome, txtPeso, txtAltura;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        if (getActivity() != null) {
            application = ((Food4fitApp) getActivity().getApplication());
            usuario = application.getUsuario();

            CircleImageView imgAvatar = view.findViewById(R.id.img_perfil_avatar);
            txtNome = view.findViewById(R.id.txt_perfil_nome);
            TextView txtEmail = view.findViewById(R.id.txt_perfil_email);
            TextView txtIdade = view.findViewById(R.id.txt_perfil_idade);
            txtPeso = view.findViewById(R.id.txt_perfil_peso);
            txtAltura = view.findViewById(R.id.txt_perfil_altura);
            edtNome = view.findViewById(R.id.edt_perfil_nome);
            EditText edtEmail = view.findViewById(R.id.edt_perfil_email);
            EditText edtRg = view.findViewById(R.id.edt_perfil_rg);
            EditText edtCpf = view.findViewById(R.id.edt_perfil_cpf);
            EditText edtDataNascimento = view.findViewById(R.id.edt_perfil_data_nascimento);
            rbMasculino = view.findViewById(R.id.rb_perfil_sexo_masculino);
            rbFeminino = view.findViewById(R.id.rb_perfil_sexo_feminino);
            edtTelefone = view.findViewById(R.id.edt_perfil_telefone);
            edtCelular = view.findViewById(R.id.edt_perfil_celular);
            FloatingActionButton fabSalvar = view.findViewById(R.id.fab_salvar_perfil);
            fabSalvar.setOnClickListener((fabView) -> this.salvarPerfil());

            txtNome.setText(String.format(Food4fitApp.LOCALE, "%s %s", usuario.getNome(), usuario.getSobrenome()));
            txtEmail.setText(usuario.getEmail());
            txtIdade.setText(calcularIdade());
            if (usuario.getAvatar() != null && !usuario.getAvatar().isEmpty()) {
                Picasso.get().load("http://10.0.2.2/food4fit/" + usuario.getAvatar()).error(R.drawable.baseline_person_24).into(imgAvatar);
            }

            edtNome.setText(String.format(Food4fitApp.LOCALE, "%s %s", usuario.getNome(), usuario.getSobrenome()));
            edtEmail.setText(usuario.getEmail());
            edtRg.setText(usuario.getRg());
            edtCpf.setText(usuario.getCpf());
            edtDataNascimento.setText(new SimpleDateFormat("dd/MM/yyyy", Food4fitApp.LOCALE).format(usuario.getDataNascimento()));
            rbMasculino.setChecked("M".equals(usuario.getGenero()));
            rbFeminino.setChecked("F".equals(usuario.getGenero()));
            edtTelefone.setText(usuario.getTelefone());
            edtCelular.setText(usuario.getCelular());
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ItemDadosSaude dados = AppDatabase.getDatabase(getContext()).getDadosSaudeDAO().selecionarAtual(usuario.getId());
        if (dados != null) {
            txtPeso.setText(String.format(Food4fitApp.LOCALE, "%.2f", dados.getPeso()));
            txtAltura.setText(String.format(Food4fitApp.LOCALE, "%.2f", dados.getAltura()));
        }
    }

    private void salvarPerfil() {
        if (!application.isNetworkAvailable()) {
            if (getActivity() != null) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Você precisa estar conectado a internet para alterar seu perfil", Snackbar.LENGTH_LONG).show();
            }

            return;
        }

        String nome = edtNome.getText().toString();
        String telefone = edtTelefone.getText().toString();
        String celular = edtCelular.getText().toString();
        if (nome.isEmpty()) {
            edtNome.setError("Preencha um nome");
            edtNome.requestFocus();
        } else if (telefone.isEmpty()) {
            edtTelefone.setError("Preencha um telefone");
            edtTelefone.requestFocus();
        } else if (celular.isEmpty()) {
            edtCelular.setError("Preencha um celular");
            edtCelular.requestFocus();
        } else {
            if (nome.contains(" ")) {
                usuario.setNome(nome.split(" ", 2)[0]);
                usuario.setSobrenome(nome.split(" ", 2)[1]);
            } else {
                usuario.setNome(nome);
                usuario.setSobrenome(" ");
            }

            usuario.setGenero(rbMasculino.isChecked() ? "M" : rbFeminino.isChecked() ? "F" : "");
            usuario.setTelefone(telefone);
            usuario.setCelular(celular);
            Call<Void> call = new RetrofitConfig().getUsuarioService().atualizar(usuario.getId(), usuario);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    AppDatabase.getDatabase(getContext()).getUsuarioDAO().update(usuario);
                    txtNome.setText(String.format(Food4fitApp.LOCALE, "%s %s", usuario.getNome(), usuario.getSobrenome()));
                    if (getActivity() != null) {
                        ((MainActivity) getActivity()).atualizarHeader();
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Perfil atualizado com sucesso", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if (getActivity() != null) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Não foi possível atualizar seu perfil, tente novamente mais tarde", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private String calcularIdade() {
        Calendar calendarNascimento = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendarNascimento.setTime(usuario.getDataNascimento());
        int idade = calendar.get(Calendar.YEAR) - calendarNascimento.get(Calendar.YEAR);
        if (calendar.get(Calendar.DAY_OF_YEAR) < calendarNascimento.get(Calendar.DAY_OF_YEAR)) {
            idade -= 1;
        }

        return String.valueOf(idade);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_perfil, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_perfil_dados_saude) {
            startActivity(new Intent(getContext(), DadosSaudeActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}