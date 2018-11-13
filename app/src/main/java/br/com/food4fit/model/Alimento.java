package br.com.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "tbl_alimento")
@Getter @Setter
public class Alimento implements Serializable {
    private @PrimaryKey(autoGenerate = true) int id;
    private @ColumnInfo(name = "id_refeicao") int idRefeicao;
    private String titulo;
    private double porcao;
    private @ColumnInfo(name = "unidade_medida") String unidadeMedida;
    private double calorias;
    private double carboidratos;
    private double proteinas;
    private double gorduras;
    private String imagem;

    public void loadImage(Context context, ImageView view) {
        if (URLUtil.isValidUrl(getImagem())) {
            Picasso.get().load(getImagem()).into(view);
        } else {
            try {
                File file = context.getFileStreamPath(getImagem());
                if (file != null) {
                    Picasso.get().load(file).into(view);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
