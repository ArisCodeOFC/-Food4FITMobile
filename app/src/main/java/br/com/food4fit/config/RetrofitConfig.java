package br.com.food4fit.config;

import br.com.food4fit.service.UnidadeMedidaService;
import br.com.food4fit.service.UsuarioService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {
    private final Retrofit retrofit;

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.15.31:3000/")
                //.baseUrl("http://10.0.2.2:3000/")
                //.baseUrl("http://172.16.16.111:3000/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public UsuarioService getUsuarioService() {
        return this.retrofit.create(UsuarioService.class);
    }

    public UnidadeMedidaService getUnidadeMedidaService() {
        return this.retrofit.create(UnidadeMedidaService.class);
    }
}
