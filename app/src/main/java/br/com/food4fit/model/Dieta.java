package br.com.food4fit.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Dieta implements Serializable {
    private @Getter @Setter @Embedded DietaEntity data;
    private @Getter @Setter @Relation(parentColumn = "id", entityColumn = "id_dieta", entity = RefeicaoEntity.class) List<Refeicao> refeicoes;
    private @Setter @Ignore boolean dadosAtualizados;
    private @Ignore double calorias;
    private @Ignore double carboidratos;
    private @Ignore double gorduras;
    private @Ignore double proteinas;

    private void atualizarDados() {
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
