package br.com.food4fit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.model.Dieta;
import br.com.food4fit.model.Refeicao;
import br.com.food4fit.model.Usuario;
import br.com.food4fit.util.AlarmUtils;

public class StartUpBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            setAlarms(null, context);
        }
    }

    public static void setAlarms(Usuario usuario, Context context) {
        Log.d("teste", "testetate");
        if (usuario == null) {
            usuario = AppDatabase.getDatabase(context).getUsuarioDAO().getUsuarioLogado();
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        AlarmUtils.cancelAllAlarms(context, intent);
        Dieta dieta = AppDatabase.getDatabase(context).getDietaDAO().getDietaAtiva(usuario.getId());
        if (dieta != null) {
            for (int i = 0; i < dieta.getRefeicoes().size(); i++) {
                Refeicao refeicao = dieta.getRefeicoes().get(i);
                Calendar calendar = Calendar.getInstance();
                String[] horario = refeicao.getData().getHorario().split(":");
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horario[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(horario[1]));
                Log.d("teste", calendar.getTime().toString());
                AlarmUtils.addAlarm(context, intent, i, calendar);
            }
        }

        Log.d("teste", AlarmUtils.getAlarmIds(context).toString());
    }
}
