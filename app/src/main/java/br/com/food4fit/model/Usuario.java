package br.com.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import br.com.food4fit.converter.DateConverter;
import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "tbl_usuario")
@TypeConverters(DateConverter.class)
@Getter @Setter
public class Usuario implements Serializable {
    private @PrimaryKey int id;
    private String nome;
    private String sobrenome;
    private String email;
    private String hash;
    private boolean login;
    private String avatar;
    private @ColumnInfo(name = "data_nascimento") Date dataNascimento;
    private String cpf;
    private String rg;
    private String genero;
    private String telefone;
    private String celular;
}
