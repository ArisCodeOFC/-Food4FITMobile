package br.com.food4fit.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import br.com.food4fit.food4fit.model.RefeicaoEntity;

@Dao
public interface RefeicaoDAO {
    @Insert
    void insert(RefeicaoEntity refeicao);
}
