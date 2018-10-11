package br.com.food4fit.food4fit.config;

import android.app.Application;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import br.com.food4fit.food4fit.model.UnidadeMedida;
import br.com.food4fit.food4fit.model.Usuario;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationData extends Application {
    private Usuario usuario;
    private UnidadeMedida[] unidadesMedida;

    @Override
    public void onCreate() {
        super.onCreate();
        getUnidadesMedida().subscribe(
            unidadeMedidas -> {},
            throwable -> Toast.makeText(getApplicationContext(), "Não foi possível se comunicar com o servidor", Toast.LENGTH_SHORT).show()
        );
    }

    public Usuario getUsuario() {
        if (usuario == null) {
            usuario = AppDatabase.getDatabase(getApplicationContext()).getUsuarioDAO().getUsuarioLogado();
        }

        return usuario;
    }

    public Observable<UnidadeMedida[]> getUnidadesMedida() {
        return Observable.create(emitter -> {
            if (unidadesMedida == null) {
                new RetrofitConfig().getUnidadeMedidaService().listar().enqueue(new Callback<UnidadeMedida[]>() {
                    @Override
                    public void onResponse(Call<UnidadeMedida[]> call, Response<UnidadeMedida[]> response) {
                        try {
                            if (response.body() != null) {
                                ObjectMapper mapper = new ObjectMapper();
                                String json = mapper.writeValueAsString(response.body());
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("unidades_medida", json).apply();

                                ApplicationData.this.unidadesMedida = response.body();
                                emitter.onNext(response.body());
                                emitter.onComplete();
                            } else {
                                emitter.onError(new NullPointerException());
                            }

                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            emitter.onError(e.getCause());
                        }
                    }

                    @Override
                    public void onFailure(Call<UnidadeMedida[]> call, Throwable throwable) {
                        throwable.printStackTrace();
                        try {
                            String json = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("unidades_medida", "");
                            ObjectMapper mapper = new ObjectMapper();
                            UnidadeMedida[] unidades = mapper.readValue(json, UnidadeMedida[].class);
                            ApplicationData.this.unidadesMedida = unidades;

                            emitter.onNext(unidades);
                            emitter.onComplete();
                        } catch (IOException e) {
                            e.printStackTrace();
                            emitter.onError(e.getCause());
                        }
                    }
                });

            } else {
                emitter.onNext(unidadesMedida);
                emitter.onComplete();
            }
        });
    }
}