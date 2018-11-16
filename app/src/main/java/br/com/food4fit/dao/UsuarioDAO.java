package br.com.food4fit.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import br.com.food4fit.model.Usuario;

@Dao
public interface UsuarioDAO {
    @Query("SELECT * FROM tbl_usuario WHERE email = :email AND login = 1")
    Usuario findByEmail(String email);

    @Update void update(Usuario usuario);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Usuario usuario);

    @Query("UPDATE tbl_usuario SET login = 1 WHERE id = :idUsuario")
    void login(long idUsuario);

    @Query("UPDATE tbl_usuario SET login = 0 WHERE id = :idUsuario")
    void logout(long idUsuario);

    @Query("SELECT * FROM tbl_usuario WHERE login = 1")
    Usuario getUsuarioLogado();
}
