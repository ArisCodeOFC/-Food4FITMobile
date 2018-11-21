package br.com.food4fit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.food4fit.Food4fitApp;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Refeicao;

public class RefeicaoAdapter extends RecyclerView.Adapter<RefeicaoAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Refeicao> list;

    public RefeicaoAdapter(Context context, List<Refeicao> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_refeicao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitulo, txtCalorias, txtHorario;

        private ViewHolder(View view) {
            super(view);
            txtTitulo = view.findViewById(R.id.txt_titulo_refeicao);
            txtCalorias = view.findViewById(R.id.txt_calorias_refeicao);
            txtHorario = view.findViewById(R.id.txt_horario_refeicao);
        }

         private void bind(Refeicao refeicao) {
            txtTitulo.setText(refeicao.getData().getTitulo());
            txtCalorias.setText(String.format(Food4fitApp.LOCALE, "%.2fkcal", refeicao.getCalorias()));
            txtHorario.setText(refeicao.getData().getHorario());
        }
    }
}