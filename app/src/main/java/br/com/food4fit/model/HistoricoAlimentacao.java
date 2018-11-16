package br.com.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import br.com.food4fit.converter.DateConverter;
import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "tbl_historico_alimentacao")
@TypeConverters(DateConverter.class)
@ForeignKey(entity = RefeicaoEntity.class, parentColumns = "idRefeicao", childColumns = "id", onDelete = ForeignKey.CASCADE)
@Getter @Setter
public class HistoricoAlimentacao {
    private @PrimaryKey(autoGenerate = true) int id;
    private @ColumnInfo(name = "id_refeicao") int idRefeicao;
    private Date data;
}