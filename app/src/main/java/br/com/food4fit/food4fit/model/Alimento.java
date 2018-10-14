package br.com.food4fit.food4fit.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;

@Entity(tableName = "tbl_alimento")
public class Alimento implements Serializable {
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "id_refeicao")
    private int idRefeicao;
    @ColumnInfo(name = "titulo")
    private String titulo;
    @ColumnInfo(name = "porcao")
    private double porcao;
    @ColumnInfo(name = "unidade_medida")
    private String unidadeMedida;
    @ColumnInfo(name = "calorias")
    private double calorias;
    @ColumnInfo(name = "carboidratos")
    private double carboidratos;
    @ColumnInfo(name = "proteinas")
    private double proteinas;
    @ColumnInfo(name = "gorduras")
    private double gorduras;
    @ColumnInfo(name = "imagem")
    private String imagem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRefeicao() {
        return idRefeicao;
    }

    public void setIdRefeicao(int idRefeicao) {
        this.idRefeicao = idRefeicao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPorcao() {
        return porcao;
    }

    public void setPorcao(double porcao) {
        this.porcao = porcao;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public double getCalorias() {
        return calorias;
    }

    public void setCalorias(double calorias) {
        this.calorias = calorias;
    }

    public double getCarboidratos() {
        return carboidratos;
    }

    public void setCarboidratos(double carboidratos) {
        this.carboidratos = carboidratos;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public double getGorduras() {
        return gorduras;
    }

    public void setGorduras(double gorduras) {
        this.gorduras = gorduras;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

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
            }
        }
    }
}
