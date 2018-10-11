package br.com.food4fit.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.food4fit.food4fit.model.Dieta;
import br.com.food4fit.food4fit.model.DietaEntity;

@Dao
public interface DietaDAO {
    @Insert
    void insert(DietaEntity dieta);

    @Query("SELECT * FROM tbl_dieta WHERE id_usuario = :idUsuario")
    List<Dieta> selectAll(int idUsuario);

    @Query("SELECT * FROM tbl_dieta WHERE id = :id")
    Dieta select(int id);
}
