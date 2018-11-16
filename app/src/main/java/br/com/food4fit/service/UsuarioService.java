package br.com.food4fit.service;

import br.com.food4fit.model.LoginModel;
import br.com.food4fit.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsuarioService {
    @POST("usuario/login")
    Call<Usuario> login(@Body LoginModel model);

    @PUT("usuario/{id}")
    Call<Void> atualizar(@Path("id") int id, @Body Usuario usuario);
}
