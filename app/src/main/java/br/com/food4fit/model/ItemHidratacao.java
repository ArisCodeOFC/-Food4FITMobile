package br.com.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import br.com.food4fit.converter.DateConverter;
import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "tbl_hidratacao")
@TypeConverters(DateConverter.class)
@Getter @Setter
public class ItemHidratacao {
    private @PrimaryKey(autoGenerate = true) int id;
    private Date data;
    private int quantidade;
    private @ColumnInfo(name = "id_usuario") int idUsuario;
}
