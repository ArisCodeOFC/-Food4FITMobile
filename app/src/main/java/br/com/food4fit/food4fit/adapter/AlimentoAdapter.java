package br.com.food4fit.food4fit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.food4fit.food4fit.R;
import br.com.food4fit.food4fit.model.Alimento;

public class AlimentoAdapter extends RecyclerView.Adapter<AlimentoAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Alimento> list;
    private final Context context;

    public AlimentoAdapter(Context context, List<Alimento> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_alimento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo;
        private ImageView imgAlimento;

        public ViewHolder(View view) {
            super(view);
            txtTitulo = view.findViewById(R.id.txt_titulo_alimento);
            imgAlimento = view.findViewById(R.id.img_alimento);
        }

        public void bind(Alimento alimento) {
            txtTitulo.setText(alimento.getTitulo());
            alimento.loadImage(context, imgAlimento);
        }
    }
}