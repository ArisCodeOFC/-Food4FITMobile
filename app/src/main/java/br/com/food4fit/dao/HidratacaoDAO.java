package br.com.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.food4fit.model.ItemHidratacao;

@Dao
public interface HidratacaoDAO {
    @Insert void insert(ItemHidratacao item);

    @Query("SELECT * FROM tbl_hidratacao WHERE STRFTIME('%Y-%m-%d', data / 1000, 'unixepoch') = DATE('NOW') AND id_usuario = :idUsuario")
    List<ItemHidratacao> selecionar(int idUsuario);
}
