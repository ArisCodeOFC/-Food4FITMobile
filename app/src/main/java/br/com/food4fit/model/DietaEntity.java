package br.com.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "tbl_dieta")
@Getter @Setter
public class DietaEntity implements Serializable {
    private @PrimaryKey(autoGenerate = true) int id;
    private String titulo;
    private String descricao;
    private double meta;
    private @ColumnInfo(name = "id_usuario") int idUsuario;
    private boolean ativa;
}
