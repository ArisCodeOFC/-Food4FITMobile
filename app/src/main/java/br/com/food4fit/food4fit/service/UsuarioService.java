package br.com.food4fit.food4fit.service;

import br.com.food4fit.food4fit.model.LoginModel;
import br.com.food4fit.food4fit.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsuarioService {
    @POST("usuario/mobile/login")
    Call<Usuario> login(@Body LoginModel model);
}
