package br.com.food4fit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.food4fit.Food4fitApp;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Compra;

public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Compra> list;

    public CompraAdapter(Context context, List<Compra> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public CompraAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_compra, parent, false);
        return new CompraAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompraAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo, txtValor, txtData, txtQuantidade;

        private ViewHolder(View view) {
            super(view);
            txtTitulo = view.findViewById(R.id.txt_compra_titulo);
            txtValor = view.findViewById(R.id.txt_compra_valor);
            txtData = view.findViewById(R.id.txt_compra_data);
            txtQuantidade = view.findViewById(R.id.txt_compra_quantidade);
        }

        public void bind(Compra item) {
            txtTitulo.setText(item.getTitulo());
            txtValor.setText(String.format(Food4fitApp.LOCALE, "R$%.2f", item.getValor() * item.getQuantidade()));
            txtData.setText(String.format(Food4fitApp.LOCALE, "%s", new SimpleDateFormat("dd/MM/yyyy - HH:mm", Food4fitApp.LOCALE).format(item.getData())));
            txtQuantidade.setText(String.format(Food4fitApp.LOCALE, "%d unidades", item.getQuantidade()));
        }
    }
}
