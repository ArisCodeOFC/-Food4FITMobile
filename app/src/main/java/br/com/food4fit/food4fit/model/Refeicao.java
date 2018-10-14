package br.com.food4fit.food4fit.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.io.Serializable;
import java.util.List;

public class Refeicao implements Serializable {
    @Embedded
    private RefeicaoEntity data;
    @Relation(parentColumn = "id", entityColumn = "id_refeicao")
    private List<Alimento> alimentos;
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

    public RefeicaoEntity getData() {
        return data;
    }

    public void setData(RefeicaoEntity data) {
        this.data = data;
    }

    public List<Alimento> getAlimentos() {
        return alimentos;
    }

    public void setAlimentos(List<Alimento> alimentos) {
        this.alimentos = alimentos;
    }

    public void atualizarDados() {
        if (!dadosAtualizados) {
            dadosAtualizados = true;
            calorias = carboidratos = gorduras = proteinas = 0;
            for (Alimento alimento : alimentos) {
                calorias += alimento.getCalorias();
                carboidratos += alimento.getCarboidratos();
                gorduras += alimento.getGorduras();
                proteinas += alimento.getProteinas();
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
