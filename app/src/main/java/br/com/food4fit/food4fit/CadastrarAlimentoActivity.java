package br.com.food4fit.food4fit;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import br.com.food4fit.food4fit.config.ApplicationData;
import br.com.food4fit.food4fit.model.UnidadeMedida;

public class CadastrarAlimentoActivity extends AppCompatActivity implements IPickResult {
    private ImageView imgAlimento;
    private UnidadeMedida[] unidadesMedida;
    private String[] unidadesMedidaString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_alimento);

        imgAlimento = findViewById(R.id.img_cadastrar_alimento);
        imgAlimento.setOnClickListener(view -> PickImageDialog.build(
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
        ).show(CadastrarAlimentoActivity.this));

        ((ApplicationData) getApplication()).getUnidadesMedida().subscribe(
                (unidadeMedidas -> preencherUnidades(unidadeMedidas)),
                (throwable -> Toast.makeText(this, "Não foi possível recuperar as unidades de medida.", Toast.LENGTH_SHORT).show())
        );

        final TextInputEditText edtUnidadeMedida = findViewById(R.id.edt_alimento_unidade);
        edtUnidadeMedida.setOnClickListener(view -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(CadastrarAlimentoActivity.this);
            adb.setSingleChoiceItems(unidadesMedidaString, 0, (d, n) -> edtUnidadeMedida.setText(unidadesMedida[n].getSigla()));
            adb.setNegativeButton("Cancelar", null);
            adb.setTitle("Escolha uma unidade");
            adb.show();
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
