package br.com.food4fit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.model.Dieta;
import br.com.food4fit.model.Refeicao;
import br.com.food4fit.model.Usuario;
import br.com.food4fit.util.AlarmUtils;

public class SetAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ||
                Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction()) ||
                Intent.ACTION_TIME_CHANGED.equals(intent.getAction())) {
            setAlarms(null, context);
        }
    }

    public static void setAlarms(Usuario usuario, Context context) {
        if (usuario == null) {
            usuario = AppDatabase.getDatabase(context).getUsuarioDAO().getUsuarioLogado();
        }

        AlarmUtils.cancelAllAlarms(context, new Intent(context, AlarmReceiver.class));
        Dieta dieta = AppDatabase.getDatabase(context).getDietaDAO().getDietaAtiva(usuario.getId());
        if (dieta != null) {
            for (int index = 0; index < dieta.getRefeicoes().size(); index++) {
                Refeicao refeicao = dieta.getRefeicoes().get(index);

                Calendar calendar = Calendar.getInstance();
                String[] horario = refeicao.getData().getHorario().split(":");
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horario[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(horario[1]));
                calendar.set(Calendar.SECOND, 0);
                if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }

                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra("refeicao", refeicao.getData().getId());
                AlarmUtils.addAlarm(context, intent, index, calendar);
            }
        }
    }
}
