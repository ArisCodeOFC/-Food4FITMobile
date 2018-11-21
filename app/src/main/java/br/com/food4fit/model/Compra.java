package br.com.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import br.com.food4fit.converter.DateConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "tbl_compra", primaryKeys = {"id_prato", "id_ordem_servico"})
@TypeConverters(DateConverter.class)
@Getter @Setter
@EqualsAndHashCode @ToString
public class Compra {
    private Date data;
    private int quantidade;
    private String titulo;
    private @EqualsAndHashCode.Include @ColumnInfo(name = "id_prato") int idPrato;
    private @EqualsAndHashCode.Include @ColumnInfo(name = "id_ordem_servico") int idOrdemServico;
    private @ColumnInfo(name = "id_usuario") int idUsuario;
    private double valor;
}
