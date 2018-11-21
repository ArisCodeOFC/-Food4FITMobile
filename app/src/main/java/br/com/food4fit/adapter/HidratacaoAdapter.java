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
import br.com.food4fit.model.ItemHidratacao;

public class HidratacaoAdapter extends RecyclerView.Adapter<HidratacaoAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<ItemHidratacao> list;

    public HidratacaoAdapter(Context context, List<ItemHidratacao> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public HidratacaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_hidratacao, parent, false);
        return new HidratacaoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HidratacaoAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtHorario, txtQuantidade;

        private ViewHolder(View view) {
            super(view);
            txtHorario = view.findViewById(R.id.txt_hidratacao_item_horario);
            txtQuantidade = view.findViewById(R.id.txt_hidratacao_item_quantidade);
        }

        private void bind(ItemHidratacao item) {
            txtHorario.setText(new SimpleDateFormat("HH:mm", Food4fitApp.LOCALE).format(item.getData()));
            txtQuantidade.setText(String.format(Food4fitApp.LOCALE, "%dmL", item.getQuantidade()));
        }
    }
}
