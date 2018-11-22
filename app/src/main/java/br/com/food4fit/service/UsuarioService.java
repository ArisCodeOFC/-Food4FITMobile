package br.com.food4fit.service;

import br.com.food4fit.model.Compra;
import br.com.food4fit.model.LoginModel;
import br.com.food4fit.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsuarioService {
    @POST("usuario/login")
    Call<Usuario> login(@Body LoginModel model);

    @PUT("usuario/{id}")
    Call<Void> atualizar(@Path("id") int id, @Body Usuario usuario);

    @GET("usuario/{id}/compra")
    Call<Compra[]> listarCompras(@Path("id") int id);

    @GET("usuario/esqueci-senha")
    Call<Void> enviarEmailRecuperacao(@Query("email") String email);

    @GET("usuario/verificar-codigo")
    Call<Void> verificarCodigo(@Query("email") String email, @Query("codigo") String codigo);

    @GET("usuario/trocar-senha")
    Call<Void> trocarSenha(@Query("email") String email, @Query("codigo") String codigo, @Query("senha") String senha);
}
