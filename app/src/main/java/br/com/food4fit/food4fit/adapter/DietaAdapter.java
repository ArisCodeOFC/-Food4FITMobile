package br.com.food4fit.food4fit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.food4fit.food4fit.R;
import br.com.food4fit.food4fit.model.DietaEntity;

public class DietaAdapter extends RecyclerView.Adapter<DietaAdapter.ViewHolder> {
    private final List<DietaEntity> dietas;
    private final LayoutInflater inflater;
    private OnItemClickListener listener;

    public DietaAdapter(Context context, List<DietaEntity> data, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.dietas = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_dieta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(dietas.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return dietas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo;

        public ViewHolder(View view) {
            super(view);
            txtTitulo = view.findViewById(R.id.txt_titulo_dieta);
        }

        public void bind(final DietaEntity dieta, final OnItemClickListener listener) {
            txtTitulo.setText(dieta.getTitulo());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(dieta);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DietaEntity item);
    }
}