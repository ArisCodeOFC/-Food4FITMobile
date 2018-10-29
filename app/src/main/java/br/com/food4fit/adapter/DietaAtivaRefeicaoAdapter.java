package br.com.food4fit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import br.com.food4fit.Food4fitApp;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.HistoricoAlimentacao;
import br.com.food4fit.model.Refeicao;

public class DietaAtivaRefeicaoAdapter extends RecyclerView.Adapter<DietaAtivaRefeicaoAdapter.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<Refeicao> list;
    private final Map<Integer, HistoricoAlimentacao> historico;
    private DietaAtivaRefeicaoAdapter.OnClick listenerAtualizarProgresso;

    public DietaAtivaRefeicaoAdapter(Context context, List<Refeicao> list, Map<Integer, HistoricoAlimentacao> historico) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.historico = historico;
    }

    public void setListenerAtualizarProgresso(OnClick listenerAtualizarProgresso) {
        this.listenerAtualizarProgresso = listenerAtualizarProgresso;
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
        private CheckBox cbConcluida;

        public ViewHolder(View view) {
            super(view);
            txtTitulo = view.findViewById(R.id.txt_dieta_refeicao_titulo);
            txtHorario = view.findViewById(R.id.txt_dieta_refeicao_horario);
            txtCalorias = view.findViewById(R.id.txt_dieta_refeicao_calorias);
            cbConcluida = view.findViewById(R.id.cb_refeicao_concluida);
        }

        public void bind(Refeicao refeicao) {
            txtTitulo.setText(refeicao.getData().getTitulo());
            txtCalorias.setText(String.format(Food4fitApp.LOCALE, "%.2fkcal", refeicao.getCalorias()));
            txtHorario.setText(refeicao.getData().getHorario());
            cbConcluida.setChecked(historico.containsKey(refeicao.getData().getId()));

            cbConcluida.setOnClickListener(v -> {
                if (!cbConcluida.isChecked()) {
                    cbConcluida.setChecked(true);
                    cbConcluida.jumpDrawablesToCurrentState();

                    new AlertDialog.Builder(context)
                            .setMessage("Deseja realmente desmarcar esta refeição? Seu progresso de hoje irá regridir")
                            .setCancelable(false)
                            .setPositiveButton("Sim", (dialog, id) -> {
                                cbConcluida.setChecked(false);
                                listenerAtualizarProgresso.onClick(refeicao,  cbConcluida.isChecked());
                            })
                            .setNegativeButton("Não", null)
                            .show();

                } else {
                    cbConcluida.setChecked(true);
                    listenerAtualizarProgresso.onClick(refeicao, cbConcluida.isChecked());
                }
            });
        }
    }

    public interface OnClick {
        void onClick(Refeicao refeicao, boolean ativo);
    }
}
