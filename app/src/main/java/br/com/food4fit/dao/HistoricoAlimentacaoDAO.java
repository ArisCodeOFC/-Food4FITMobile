package br.com.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.food4fit.model.HistoricoAlimentacao;

@Dao
public interface HistoricoAlimentacaoDAO {
    @Insert
    void insert(HistoricoAlimentacao entry);

    @Query("SELECT * FROM tbl_historico_alimentacao WHERE STRFTIME('%Y-%m-%d', data / 1000, 'unixepoch') = DATE('now')")
    List<HistoricoAlimentacao> getHistoricoDia();
}
