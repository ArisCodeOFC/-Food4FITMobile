package br.com.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import br.com.food4fit.model.Alimento;

@Dao
public interface AlimentoDAO {
    @Insert void insert(Alimento alimento);
    @Update void update(Alimento alimento);
    @Delete void delete(Alimento alimento);
}
