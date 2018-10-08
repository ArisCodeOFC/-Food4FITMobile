package br.com.food4fit.food4fit.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.io.Serializable;
import java.util.List;

public class Refeicao implements Serializable {
    @Embedded
    private RefeicaoEntity refeicao;
    @Relation(parentColumn = "id", entityColumn = "id_refeicao")
    private List<Alimento> alimentos;

    public RefeicaoEntity getRefeicao() {
        return refeicao;
    }

    public void setRefeicao(RefeicaoEntity refeicao) {
        this.refeicao = refeicao;
    }

    public List<Alimento> getAlimentos() {
        return alimentos;
    }

    public void setAlimentos(List<Alimento> alimentos) {
        this.alimentos = alimentos;
    }
}
