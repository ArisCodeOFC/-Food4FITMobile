package br.com.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "tbl_refeicao")
@Getter @Setter
public class RefeicaoEntity implements Serializable {
    private @PrimaryKey(autoGenerate = true) int id;
    private @ColumnInfo(name = "id_dieta") int idDieta;
    private String titulo;
    private String descricao;
    private String horario;
}
