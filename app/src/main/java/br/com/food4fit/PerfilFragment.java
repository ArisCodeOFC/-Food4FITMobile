package br.com.food4fit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        if (getActivity() != null) {
            Usuario usuario = ((Food4fitApp) getActivity().getApplication()).getUsuario();

            CircleImageView imgAvatar = view.findViewById(R.id.img_perfil_avatar);
            TextView txtNome = view.findViewById(R.id.txt_perfil_nome);
            TextView txtEmail = view.findViewById(R.id.txt_perfil_email);
            EditText edtNome = view.findViewById(R.id.edt_perfil_nome);
            EditText edtEmail = view.findViewById(R.id.edt_perfil_email);
            EditText edtRg = view.findViewById(R.id.edt_perfil_rg);
            EditText edtCpf = view.findViewById(R.id.edt_perfil_cpf);
            EditText edtDataNascimento = view.findViewById(R.id.edt_perfil_data_nascimento);

            txtNome.setText(String.format(Food4fitApp.LOCALE, "%s %s", usuario.getNome(), usuario.getSobrenome()));
            txtEmail.setText(usuario.getEmail());
            if (usuario.getAvatar() != null && !usuario.getAvatar().isEmpty()) {
                Picasso.get().load("http://10.0.2.2/food4fit/" + usuario.getAvatar()).error(R.drawable.baseline_person_24).into(imgAvatar);
            }

            edtNome.setText(String.format(Food4fitApp.LOCALE, "%s %s", usuario.getNome(), usuario.getSobrenome()));
            edtEmail.setText(usuario.getEmail());
            edtRg.setText(usuario.getRg());
            edtCpf.setText(usuario.getCpf());
            edtDataNascimento.setText(new SimpleDateFormat("dd/MM/yyyy", Food4fitApp.LOCALE).format(usuario.getDataNascimento()));
        }

        return view;
    }
}