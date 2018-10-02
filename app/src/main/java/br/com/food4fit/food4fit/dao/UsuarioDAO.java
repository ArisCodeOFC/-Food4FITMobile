package br.com.food4fit.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import br.com.food4fit.food4fit.model.Usuario;

@Dao
public interface UsuarioDAO {
    @Query("SELECT * FROM tbl_usuario WHERE email = :email")
    public Usuario findByEmail(String email);

    @Insert
    public void insert(Usuario usuario);

    @Query("DELETE FROM tbl_usuario WHERE id = :idUsuario")
    public void delete(long idUsuario);
}
