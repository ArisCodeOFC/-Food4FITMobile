package br.com.food4fit.food4fit;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.IOException;

import br.com.food4fit.food4fit.config.RetrofitConfig;
import br.com.food4fit.food4fit.model.UnidadeMedida;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastrarAlimentoActivity extends AppCompatActivity implements IPickResult {
    private ImageView imgAlimento;
    private UnidadeMedida[] unidadesMedida;
    private String[] unidadesMedidaString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_alimento);

        imgAlimento = findViewById(R.id.img_cadastrar_alimento);
        imgAlimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(
                        new PickSetup()
                                .setTitle("Selecione uma imagem")
                                .setCancelText("Cancelar")
                                .setProgressText("Carregando")
                                .setCameraButtonText("Câmera")
                                .setGalleryButtonText("Galeria")
                                .setCameraIcon(R.mipmap.camera_colored)
                                .setGalleryIcon(R.mipmap.gallery_colored)
                                .setIconGravity(Gravity.TOP)
                                .setButtonOrientation(LinearLayout.HORIZONTAL)
                ).show(CadastrarAlimentoActivity.this);
            }
        });

        new RetrofitConfig().getUnidadeMedidaService().listar().enqueue(new Callback<UnidadeMedida[]>() {
            @Override
            public void onResponse(Call<UnidadeMedida[]> call, Response<UnidadeMedida[]> response) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(response.body());
                    PreferenceManager.getDefaultSharedPreferences(CadastrarAlimentoActivity.this).edit().putString("unidades_medida", json).apply();
                    preencherUnidades(response.body());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UnidadeMedida[]> call, Throwable throwable) {
                throwable.printStackTrace();
                try {
                    String json = PreferenceManager.getDefaultSharedPreferences(CadastrarAlimentoActivity.this).getString("unidades_medida", "");
                    ObjectMapper mapper = new ObjectMapper();
                    UnidadeMedida[] unidades = mapper.readValue(json, UnidadeMedida[].class);
                    preencherUnidades(unidades);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        final TextInputEditText edtUnidadeMedida = findViewById(R.id.edt_alimento_unidade);
        edtUnidadeMedida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(CadastrarAlimentoActivity.this);
                adb.setSingleChoiceItems(unidadesMedidaString, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int n) {
                        edtUnidadeMedida.setText(unidadesMedida[n].getSigla());
                    }
                });

                adb.setNegativeButton("Cancelar", null);
                adb.setTitle("Escolha uma unidade");
                adb.show();
            }
        });
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {
            Bitmap bitmap = pickResult.getBitmap();
            if (bitmap.getHeight() > 512) {
                int newHeight = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 512, newHeight, true);
                imgAlimento.setImageBitmap(scaledBitmap);
            } else {
                imgAlimento.setImageBitmap(bitmap);
            }
        }
    }

    private void preencherUnidades(UnidadeMedida[] unidades) {
        unidadesMedidaString = new String[unidades.length];
        for (int i = 0; i < unidadesMedidaString.length; i++) {
            unidadesMedidaString[i] = unidades[i].getUnidadeMedida() + " (" + unidades[i].getSigla() + ")";
        }

        unidadesMedida = unidades;
    }
}
