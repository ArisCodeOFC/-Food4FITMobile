package br.com.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import br.com.food4fit.model.Refeicao;
import br.com.food4fit.model.RefeicaoEntity;

@Dao
public interface RefeicaoDAO {
    @Insert long insert(RefeicaoEntity refeicao);
    @Update void update(RefeicaoEntity refeicao);
    @Delete void delete(RefeicaoEntity refeicao);

    @Query("SELECT * FROM tbl_refeicao WHERE id = :id")
    @Transaction Refeicao select(int id);

    @Query("SELECT * FROM tbl_refeicao WHERE id = :id")
    RefeicaoEntity selectData(int id);
}
