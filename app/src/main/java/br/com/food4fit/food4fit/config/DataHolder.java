package br.com.food4fit.food4fit.config;

public class DataHolder {
    public static DataHolder INSTANCE;

    public static DataHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataHolder();
        }

        return INSTANCE;
    }


}
