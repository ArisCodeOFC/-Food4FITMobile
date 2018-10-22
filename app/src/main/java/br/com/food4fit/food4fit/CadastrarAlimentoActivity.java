package br.com.food4fit.food4fit;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.food4fit.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.model.Alimento;
import br.com.food4fit.food4fit.model.Refeicao;
import br.com.food4fit.food4fit.model.UnidadeMedida;

public class CadastrarAlimentoActivity extends AppCompatActivity implements IPickResult {
    private ImageView imgAlimento;
    private UnidadeMedida[] unidadesMedida;
    private String[] unidadesMedidaString;
    private StorageReference storageRef;
    private Bitmap imgBitmap;
    private EditText edtTitulo, edtPorcao, edtUnidadeMedida, edtCalorias, edtCarboidratos, edtProteinas, edtGorduras;
    private Refeicao refeicao;
    private Alimento alimento;
    private int unidadeSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_alimento);

        imgAlimento = findViewById(R.id.img_cadastrar_alimento);
        edtTitulo = findViewById(R.id.edt_alimento_titulo);
        edtPorcao = findViewById(R.id.edt_alimento_porcao);
        edtUnidadeMedida = findViewById(R.id.edt_alimento_unidade);
        edtCalorias = findViewById(R.id.edt_alimento_calorias);
        edtCarboidratos = findViewById(R.id.edt_alimento_carboidratos);
        edtProteinas = findViewById(R.id.edt_alimento_proteinas);
        edtGorduras = findViewById(R.id.edt_alimento_gorduras);

        Intent intent = getIntent();
        if (intent != null) {
            refeicao = (Refeicao) intent.getSerializableExtra("refeicao");
            alimento = (Alimento) intent.getSerializableExtra("alimento");
            setTitle(refeicao.getData().getTitulo());

            if (alimento != null) {
                alimento.loadImage(this, imgAlimento);
                edtTitulo.setText(alimento.getTitulo());
                edtPorcao.setText(String.valueOf(alimento.getPorcao()));
                edtUnidadeMedida.setText(alimento.getUnidadeMedida());
                edtCalorias.setText(String.valueOf(alimento.getCalorias()));
                edtCarboidratos.setText(String.valueOf(alimento.getCarboidratos()));
                edtProteinas.setText(String.valueOf(alimento.getProteinas()));
                edtGorduras.setText(String.valueOf(alimento.getGorduras()));
            }
        }

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
        ).show(this));

        ((Food4fitApp) getApplication()).getUnidadesMedida().subscribe(
            (this::preencherUnidades),
            (throwable -> Toast.makeText(this, "Não foi possível recuperar as unidades de medida.", Toast.LENGTH_SHORT).show())
        );

        edtUnidadeMedida.setOnClickListener(view -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setSingleChoiceItems(unidadesMedidaString, unidadeSelecionada, (dialog, selected) -> {
                unidadeSelecionada = selected;
                edtUnidadeMedida.setText(unidadesMedida[selected].getSigla());
                dialog.dismiss();
            });

            adb.setNegativeButton("Cancelar", null);
            adb.setTitle("Escolha uma unidade");
            adb.show();
        });

        Button btnSalvar = findViewById(R.id.btn_alimento_salvar);
        btnSalvar.setOnClickListener(view -> salvarAlimento());

        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {
            Bitmap bitmap = pickResult.getBitmap();
            if (bitmap.getHeight() > 512) {
                int newHeight = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 512, newHeight, true);
                imgAlimento.setImageBitmap(scaledBitmap);
                imgBitmap = scaledBitmap;
            } else {
                imgAlimento.setImageBitmap(bitmap);
                imgBitmap = bitmap;
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

    private void salvarAlimento() {
        String titulo = edtTitulo.getText().toString();
        String porcao = edtPorcao.getText().toString();
        String unidadeMedida = edtUnidadeMedida.getText().toString();
        String calorias = edtCalorias.getText().toString();
        String carboidratos = edtCarboidratos.getText().toString();
        String proteinas = edtProteinas.getText().toString();
        String gorduras = edtGorduras.getText().toString();

        if (imgBitmap == null && alimento == null) {
            Snackbar.make(findViewById(android.R.id.content), "Selecione uma imagem", Snackbar.LENGTH_SHORT).show();
        } else if (titulo.isEmpty()) {
            edtTitulo.setError("Preencha um título");
            edtTitulo.requestFocus();
        } else if (porcao.isEmpty()) {
            edtPorcao.setError("Preencha uma porção");
            edtPorcao.requestFocus();
        } else if (unidadeMedida.isEmpty()) {
            edtUnidadeMedida.setError("Selecione uma unidade de medida");
            edtUnidadeMedida.requestFocus();
        } else if (calorias.isEmpty()) {
            edtCalorias.setError("Preencha um valor");
            edtCalorias.requestFocus();
        } else if (carboidratos.isEmpty()) {
            edtCarboidratos.setError("Preencha um valor");
            edtCarboidratos.requestFocus();
        } else if (proteinas.isEmpty()) {
            edtProteinas.setError("Preencha um valor");
            edtProteinas.requestFocus();
        } else if (gorduras.isEmpty()) {
            edtGorduras.setError("Preencha um valor");
            edtGorduras.requestFocus();
        } else {
            Alimento alimento = new Alimento();
            alimento.setTitulo(titulo);
            alimento.setPorcao(Double.parseDouble(porcao));
            alimento.setUnidadeMedida(unidadeMedida);
            alimento.setCalorias(Double.parseDouble(calorias));
            alimento.setCarboidratos(Double.parseDouble(carboidratos));
            alimento.setProteinas(Double.parseDouble(proteinas));
            alimento.setGorduras(Double.parseDouble(gorduras));
            alimento.setIdRefeicao(refeicao.getData().getId());
            if (this.alimento != null) {
                alimento.setId(this.alimento.getId());
            }

            if (imgBitmap != null) {
                String caminhoImagem = UUID.randomUUID().toString();
                StorageReference imageRef = storageRef.child("images/" + caminhoImagem);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imgBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byte[] imageData = stream.toByteArray();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(R.layout.progress_dialog);
                builder.setCancelable(false);
                Dialog dialog = builder.create();
                dialog.show();

                if (!(((Food4fitApp) getApplication()).isNetworkAvailable())) {
                    salvarImagemArquivo(caminhoImagem, imageData);
                    alimento.setImagem(caminhoImagem);
                    dialog.dismiss();
                    finalizarCadastro(alimento);
                } else {
                    imageRef.putBytes(imageData)
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                                dialog.dismiss();
                                Toast.makeText(this, "Não foi possível processar a imagem", Toast.LENGTH_SHORT).show();
                            })
                            .addOnCanceledListener(() -> {
                                dialog.dismiss();
                                Toast.makeText(this, "Não foi possível processar a imagem", Toast.LENGTH_SHORT).show();
                            })
                            .addOnCompleteListener(task -> {
                                if (task.isComplete()) {
                                    if (task.isSuccessful()) {
                                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                            alimento.setImagem(uri.toString());
                                            dialog.dismiss();
                                            finalizarCadastro(alimento);
                                        }).addOnFailureListener(e -> {
                                            e.printStackTrace();
                                            dialog.dismiss();
                                            Toast.makeText(this, "Não foi possível processar a imagem", Toast.LENGTH_SHORT).show();
                                        });

                                    } else {
                                        salvarImagemArquivo(caminhoImagem, imageData);
                                        alimento.setImagem(caminhoImagem);
                                        dialog.dismiss();
                                        finalizarCadastro(alimento);
                                    }

                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(this, "Não foi possível processar a imagem", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            } else {
                alimento.setImagem(this.alimento.getImagem());
                finalizarCadastro(alimento);
            }
        }
    }
    
    private void salvarImagemArquivo(String caminhoImagem, byte[] data) {
        try {
            FileOutputStream fos = openFileOutput(caminhoImagem, MODE_PRIVATE);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void finalizarCadastro(Alimento alimento) {
        if (alimento.getId() != 0) {
            AppDatabase.getDatabase(this).getAlimentoDAO().update(alimento);
        } else {
            AppDatabase.getDatabase(this).getAlimentoDAO().insert(alimento);
        }

        finish();
    }
}
