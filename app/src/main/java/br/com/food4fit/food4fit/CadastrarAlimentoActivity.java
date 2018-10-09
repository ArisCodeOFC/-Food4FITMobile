package br.com.food4fit.food4fit;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

public class CadastrarAlimentoActivity extends AppCompatActivity implements IPickResult {
    private ImageView imgAlimento;

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
}
