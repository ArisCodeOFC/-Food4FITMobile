package br.com.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import br.com.food4fit.converter.DateConverter;

@Entity(tableName = "tbl_historico_alimentacao")
@TypeConverters(DateConverter.class)
@ForeignKey(entity = RefeicaoEntity.class, parentColumns = "idRefeicao", childColumns = "id", onDelete = ForeignKey.CASCADE)
public class HistoricoAlimentacao {
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "id_refeicao")
    private int idRefeicao;
    @ColumnInfo(name = "data")
    private Date data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRefeicao() {
        return idRefeicao;
    }

    public void setIdRefeicao(int idRefeicao) {
        this.idRefeicao = idRefeicao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}