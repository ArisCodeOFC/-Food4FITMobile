package br.com.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import br.com.food4fit.model.ItemDadosSaude;

@Dao
public interface DadosSaudeDAO {
    @Insert void insert(ItemDadosSaude item);

    @Query("SELECT * FROM tbl_dados_saude WHERE id_usuario = :idUsuario ORDER BY data DESC LIMIT 1")
    ItemDadosSaude selecionarAtual(int idUsuario);
}
