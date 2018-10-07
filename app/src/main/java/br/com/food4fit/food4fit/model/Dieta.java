package br.com.food4fit.food4fit.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class Dieta {
    @Embedded
    private Dieta dieta;
    @Relation(parentColumn = "id", entityColumn = "id_dieta")
    private List<RefeicaoEntity> refeicoes;

    public Dieta getDieta() {
        return dieta;
    }

    public List<RefeicaoEntity> getRefeicoes() {
        return refeicoes;
    }
}
