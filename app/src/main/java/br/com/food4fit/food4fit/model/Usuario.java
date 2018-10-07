package br.com.food4fit.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tbl_usuario")
public class Usuario implements Serializable {
    @ColumnInfo(name = "id") @PrimaryKey
    private int id;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "sobrenome")
    private String sobrenome;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "hash")
    private String hash;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
