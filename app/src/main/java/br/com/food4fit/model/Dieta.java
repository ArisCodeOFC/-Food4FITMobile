package br.com.food4fit.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.io.Serializable;
import java.util.List;

public class Dieta implements Serializable {
    @Embedded
    private DietaEntity data;
    @Relation(parentColumn = "id", entityColumn = "id_dieta", entity = RefeicaoEntity.class)
    private List<Refeicao> refeicoes;
    @Ignore
    private boolean dadosAtualizados;
    @Ignore
    private double calorias;
    @Ignore
    private double carboidratos;
    @Ignore
    private double gorduras;
    @Ignore
    private double proteinas;

    public DietaEntity getData() {
        return data;
    }

    public void setData(DietaEntity data) {
        this.data = data;
    }

    public List<Refeicao> getRefeicoes() {
        return refeicoes;
    }

    public void setRefeicoes(List<Refeicao> refeicoes) {
        this.refeicoes = refeicoes;
    }

    public void atualizarDados() {
        if (!dadosAtualizados) {
            dadosAtualizados = true;
            calorias = carboidratos = gorduras = proteinas = 0;
            for (Refeicao refeicao : refeicoes) {
                calorias += refeicao.getCalorias();
                carboidratos += refeicao.getCarboidratos();
                gorduras += refeicao.getGorduras();
                proteinas += refeicao.getProteinas();
            }
        }
    }

    public void setDadosAtualizados(boolean dadosAtualizados) {
        this.dadosAtualizados = dadosAtualizados;
    }

    public double getCalorias() {
        atualizarDados();
        return calorias;
    }

    public double getCarboidratos() {
        atualizarDados();
        return carboidratos;
    }

    public double getGorduras() {
        atualizarDados();
        return gorduras;
    }

    public double getProteinas() {
        atualizarDados();
        return proteinas;
    }
}
