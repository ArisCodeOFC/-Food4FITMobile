package br.com.food4fit;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import br.com.food4fit.food4fit.BuildConfig;
import br.com.food4fit.food4fit.R;

public class ConfiguracoesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference prefVersao = findPreference("pref_versao");
        prefVersao.setTitle("Versão do app: " + BuildConfig.VERSION_NAME);

        Preference prefCodigoFonte = findPreference("pref_codigo_fonte");
        prefCodigoFonte.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ArisCodeOFC/Food4FITMobile")));
            return false;
        });

        Preference prefClassificar = findPreference("pref_classificar");
        prefClassificar.setOnPreferenceClickListener(preference -> {
            if (getContext() != null) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
                }
            }

            return false;
        });

        if (getContext() != null) {
            PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("dark_mode")) {
            if (getActivity() != null) {
                getActivity().setTheme(R.style.AppTheme_Dark);
                getActivity().recreate();
            }
        }
    }
}