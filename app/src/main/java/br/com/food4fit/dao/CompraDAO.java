package br.com.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.food4fit.model.Compra;

@Dao
public interface CompraDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE) void insert(Compra compra);
    @Delete void remove(Compra compra);

    @Query("SELECT * FROM tbl_compra WHERE id_prato = :idPrato AND id_ordem_servico = :idOrdemServico")
    Compra selecionar(int idPrato, int idOrdemServico);

    @Query("SELECT * FROM tbl_compra WHERE id_usuario = :idUsuario")
    List<Compra> selecionarTodos(int idUsuario);
}
