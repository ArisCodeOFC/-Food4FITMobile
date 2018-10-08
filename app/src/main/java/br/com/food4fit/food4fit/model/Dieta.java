package br.com.food4fit.food4fit.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.io.Serializable;
import java.util.List;

public class Dieta implements Serializable {
    @Embedded
    private DietaEntity dieta;
    @Relation(parentColumn = "id", entityColumn = "id_dieta")
    private List<RefeicaoEntity> refeicoes;

    public DietaEntity getDieta() {
        return dieta;
    }

    public void setDieta(DietaEntity dieta) {
        this.dieta = dieta;
    }

    public List<RefeicaoEntity> getRefeicoes() {
        return refeicoes;
    }

    public void setRefeicoes(List<RefeicaoEntity> refeicoes) {
        this.refeicoes = refeicoes;
    }
}
