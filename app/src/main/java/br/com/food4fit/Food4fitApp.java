package br.com.food4fit;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.model.Alimento;
import br.com.food4fit.model.Dieta;
import br.com.food4fit.model.Refeicao;
import br.com.food4fit.model.UnidadeMedida;
import br.com.food4fit.model.Usuario;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Food4fitApp extends Application {
    public static final Locale LOCALE = new Locale("pt","BR");
    private Usuario usuario;
    private UnidadeMedida[] unidadesMedida;
    private StorageReference storageRef;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        super.onCreate();
        getUnidadesMedida().subscribe(
            unidadeMedidas -> {},
            throwable -> Toast.makeText(getApplicationContext(), "Não foi possível se comunicar com o servidor", Toast.LENGTH_SHORT).show()
        );

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }

    public Usuario getUsuario() {
        if (usuario == null) {
            usuario = AppDatabase.getDatabase(getApplicationContext()).getUsuarioDAO().getUsuarioLogado();
        }

        return usuario;
    }

    public void sincronizarImagens() {
        if (isNetworkAvailable()) {
            List<Dieta> dietas = AppDatabase.getDatabase(getApplicationContext()).getDietaDAO().selectAll(getUsuario().getId());
            for (Dieta dieta : dietas) {
                for (Refeicao refeicao : dieta.getRefeicoes()) {
                    for (Alimento alimento : refeicao.getAlimentos()) {
                        if (!URLUtil.isValidUrl(alimento.getImagem())) {
                            try {
                                FileInputStream fis = openFileInput(alimento.getImagem());
                                StorageReference imageRef = storageRef.child("images/" + alimento.getImagem());
                                imageRef.putStream(fis)
                                        .addOnCompleteListener(task -> {
                                            if (task.isComplete() && task.isSuccessful()) {
                                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                                    alimento.setImagem(uri.toString());
                                                    AppDatabase.getDatabase(this).getAlimentoDAO().update(alimento);
                                                });
                                            }
                                        });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
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

                                Food4fitApp.this.unidadesMedida = response.body();
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
                            Food4fitApp.this.unidadesMedida = unidades;

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

    public static boolean isDarkMode(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dark_mode", false);
    }
}