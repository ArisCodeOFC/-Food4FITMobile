package br.com.food4fit.food4fit.config;

import br.com.food4fit.food4fit.service.UsuarioService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {
    private final Retrofit retrofit;

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                //.baseUrl("http://10.0.2.2/food4fit/api/v1/")
                .baseUrl("http://172.16.16.111/tcc/api/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public UsuarioService getUsuarioService() {
        return this.retrofit.create(UsuarioService.class);
    }
}
