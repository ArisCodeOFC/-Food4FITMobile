package br.com.food4fit.food4fit.model;

import java.io.Serializable;

public class Dieta implements Serializable {
    private int id;
    private String titulo;

    public Dieta(int id, String titulo) {
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
