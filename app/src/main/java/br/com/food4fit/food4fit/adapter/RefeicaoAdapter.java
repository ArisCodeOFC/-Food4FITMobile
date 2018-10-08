package br.com.food4fit.food4fit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.food4fit.food4fit.R;
import br.com.food4fit.food4fit.model.RefeicaoEntity;

public class RefeicaoAdapter extends RecyclerView.Adapter<RefeicaoAdapter.ViewHolder> {
    private final List<RefeicaoEntity> refeicoes;
    private final LayoutInflater inflater;
    private OnItemClickListener listener;

    public RefeicaoAdapter(Context context, List<RefeicaoEntity> data, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.refeicoes = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_refeicao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(refeicoes.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return refeicoes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo;
        private TextView txtCalorias;
        private TextView txtHorario;

        public ViewHolder(View view) {
            super(view);
            txtTitulo = view.findViewById(R.id.txt_titulo_refeicao);
            txtCalorias = view.findViewById(R.id.txt_calorias_refeicao);
            txtHorario = view.findViewById(R.id.txt_horario_refeicao);
        }

        public void bind(final RefeicaoEntity refeicao, final OnItemClickListener listener) {
            txtTitulo.setText(refeicao.getTitulo());
            txtHorario.setText(refeicao.getHorario());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(refeicao);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RefeicaoEntity item);
    }
}