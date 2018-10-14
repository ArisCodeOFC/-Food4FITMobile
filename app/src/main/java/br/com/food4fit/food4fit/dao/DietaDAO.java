package br.com.food4fit.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.food4fit.food4fit.model.Dieta;
import br.com.food4fit.food4fit.model.DietaEntity;

@Dao
public interface DietaDAO {
    @Insert
    void insert(DietaEntity dieta);

    @Query("SELECT * FROM tbl_dieta WHERE id_usuario = :idUsuario")
    @Transaction
    List<Dieta> selectAll(int idUsuario);

    @Query("SELECT * FROM tbl_dieta WHERE id = :id")
    @Transaction
    Dieta select(int id);

    @Update
    void update(DietaEntity dieta);

    @Delete
    void delete(DietaEntity dieta);
}
