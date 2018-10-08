package br.com.food4fit.food4fit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.food4fit.food4fit.R;
import br.com.food4fit.food4fit.model.Alimento;

public class AlimentoAdapter extends RecyclerView.Adapter<AlimentoAdapter.ViewHolder> {
    private final List<Alimento> alimentos;
    private final LayoutInflater inflater;
    private OnItemClickListener listener;

    public AlimentoAdapter(Context context, List<Alimento> data, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.alimentos = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_alimento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(alimentos.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return alimentos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo;

        public ViewHolder(View view) {
            super(view);
            txtTitulo = view.findViewById(R.id.txt_titulo_alimento);
        }

        public void bind(final Alimento alimento, final OnItemClickListener listener) {
            txtTitulo.setText(alimento.getTitulo());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(alimento);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Alimento item);
    }
}