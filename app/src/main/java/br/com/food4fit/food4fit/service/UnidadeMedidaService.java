package br.com.food4fit.food4fit.service;

import br.com.food4fit.food4fit.model.UnidadeMedida;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UnidadeMedidaService {
    @GET("unidade-medida")
    Call<UnidadeMedida[]> listar();
}
