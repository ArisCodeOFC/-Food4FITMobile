package br.com.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import br.com.food4fit.converter.DateConverter;

@Entity(tableName = "tbl_acompanhamento")
@TypeConverters(DateConverter.class)
public class ItemAcompanhamento {
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "data")
    private Date data;
    @ColumnInfo(name = "calorias")
    private double calorias;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getCalorias() {
        return calorias;
    }

    public void setCalorias(double calorias) {
        this.calorias = calorias;
    }
}
