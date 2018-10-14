package br.com.food4fit.food4fit.config;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.food4fit.food4fit.dao.AlimentoDAO;
import br.com.food4fit.food4fit.dao.DietaDAO;
import br.com.food4fit.food4fit.dao.RefeicaoDAO;
import br.com.food4fit.food4fit.dao.UsuarioDAO;
import br.com.food4fit.food4fit.model.Alimento;
import br.com.food4fit.food4fit.model.DietaEntity;
import br.com.food4fit.food4fit.model.RefeicaoEntity;
import br.com.food4fit.food4fit.model.Usuario;

@Database(entities = {Usuario.class, DietaEntity.class, RefeicaoEntity.class, Alimento.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract UsuarioDAO getUsuarioDAO();
    public abstract DietaDAO getDietaDAO();
    public abstract RefeicaoDAO getRefeicaoDAO();
    public abstract AlimentoDAO getAlimentoDAO();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "food4fit")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
        }

        return INSTANCE;
    }
}