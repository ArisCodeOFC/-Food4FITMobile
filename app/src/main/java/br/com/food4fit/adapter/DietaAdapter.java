package br.com.food4fit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.food4fit.Food4fitApp;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Dieta;

public class DietaAdapter extends RecyclerView.Adapter<DietaAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Dieta> list;

    public DietaAdapter(Context context, List<Dieta> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_dieta, parent, false);
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
        private TextView txtTitulo, txtCalorias, txtCarboidratos, txtProteinas;

        public ViewHolder(View view) {
            super(view);
            txtTitulo = view.findViewById(R.id.txt_titulo_dieta);
            txtCalorias = view.findViewById(R.id.txt_calorias_dieta);
            txtCarboidratos = view.findViewById(R.id.txt_carboidratos_dieta);
            txtProteinas = view.findViewById(R.id.txt_proteinas_dieta);
        }

        public void bind(Dieta dieta) {
            txtTitulo.setText(dieta.getData().getTitulo());
            txtCalorias.setText(String.format(Food4fitApp.LOCALE, "%.2fkcal", dieta.getCalorias()));
            txtCarboidratos.setText(String.format(Food4fitApp.LOCALE, "%.2fg carb", dieta.getCarboidratos()));
            txtProteinas.setText(String.format(Food4fitApp.LOCALE, "%.2fg prot", dieta.getProteinas()));
        }
    }
}