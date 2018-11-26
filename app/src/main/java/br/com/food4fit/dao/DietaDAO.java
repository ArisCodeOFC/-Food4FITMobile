package br.com.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.food4fit.model.Dieta;
import br.com.food4fit.model.DietaEntity;

@Dao
public interface DietaDAO {
    @Insert long insert(DietaEntity dieta);
    @Update void update(DietaEntity dieta);
    @Delete void delete(DietaEntity dieta);

    @Query("SELECT * FROM tbl_dieta WHERE id_usuario = :idUsuario")
    @Transaction List<Dieta> selectAll(int idUsuario);

    @Query("SELECT * FROM tbl_dieta WHERE id = :id")
    @Transaction Dieta select(int id);

    @Query("SELECT * FROM tbl_dieta WHERE id_usuario = :idUsuario AND ativa = 1 LIMIT 1")
    @Transaction Dieta getDietaAtiva(int idUsuario);

    @Query("UPDATE tbl_dieta SET ativa = 0 WHERE id_usuario = :idUsuario")
    void desativarDietas(int idUsuario);
}
