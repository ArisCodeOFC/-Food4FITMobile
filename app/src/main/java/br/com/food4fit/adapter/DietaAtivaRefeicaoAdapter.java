package br.com.food4fit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Refeicao;

public class DietaAtivaRefeicaoAdapter extends RecyclerView.Adapter<DietaAtivaRefeicaoAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Refeicao> list;

    public DietaAtivaRefeicaoAdapter(Context context, List<Refeicao> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public DietaAtivaRefeicaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_dieta_ativa, parent, false);
        return new DietaAtivaRefeicaoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DietaAtivaRefeicaoAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo, txtCalorias, txtHorario;

        public ViewHolder(View view) {
            super(view);
            txtTitulo = view.findViewById(R.id.txt_dieta_refeicao_titulo);
            txtHorario = view.findViewById(R.id.txt_dieta_refeicao_horario);
            txtCalorias = view.findViewById(R.id.txt_dieta_refeicao_calorias);
        }

        public void bind(Refeicao refeicao) {
            txtTitulo.setText(refeicao.getData().getTitulo());
            txtCalorias.setText(String.format(new Locale("pt","BR"), "%.2fkcal", refeicao.getCalorias()));
            txtHorario.setText(refeicao.getData().getHorario());
        }
    }
}
