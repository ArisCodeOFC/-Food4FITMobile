package br.com.food4fit.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class RefeicaoEntity {
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "titulo")
    private String titulo;
    @ColumnInfo(name = "id_dieta")
    private int idDieta;

    public RefeicaoEntity(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
