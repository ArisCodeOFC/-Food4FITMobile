package br.com.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.Date;

import br.com.food4fit.converter.DateConverter;
import br.com.food4fit.model.ItemAcompanhamento;

@Dao
@TypeConverters(DateConverter.class)
public interface AcompanhamentoDAO {
    @Insert
    void insert(ItemAcompanhamento item);

    @Update
    void update(ItemAcompanhamento item);

    @Query("SELECT * FROM tbl_acompanhamento WHERE STRFTIME('%Y-%m-%d', data / 1000, 'unixepoch') = DATE('NOW')")
    ItemAcompanhamento selecionarDia();

    @Query("SELECT * FROM tbl_acompanhamento WHERE STRFTIME('%Y-%m-%d', data / 1000, 'unixepoch') = STRFTIME('%Y-%m-%d', :data / 1000, 'unixepoch')")
    ItemAcompanhamento selecionarDia(Date data);
}
