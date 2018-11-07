package br.com.food4fit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.food4fit.broadcast.AlarmReceiver;
import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Usuario;
import br.com.food4fit.util.AlarmUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AccountManager accountManager;
    private DrawerLayout drawer;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Food4fitApp.isDarkMode()) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }

        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("usuario")) {
            usuario = (Usuario) intent.getSerializableExtra("usuario");
        } else {
            usuario = ((Food4fitApp) getApplication()).getUsuario();
        }

        if (usuario == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        accountManager = AccountManager.get(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (navigationView.getMenu().size() > 0) {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }

        TextView txtDrawerNome = navigationView.getHeaderView(0).findViewById(R.id.txt_drawer_nome);
        TextView txtDrawerEmail = navigationView.getHeaderView(0).findViewById(R.id.txt_drawer_email);
        txtDrawerNome.setText(String.format(Food4fitApp.LOCALE, "%s %s", usuario.getNome(), usuario.getSobrenome()));
        txtDrawerEmail.setText(usuario.getEmail());

        ((Food4fitApp) getApplication()).sincronizarImagens();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_sair) {
            logout();
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, new HomeFragment()).commit();
        } else if (id == R.id.nav_dietas) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, new DietasFragment()).commit();
        } else if (id == R.id.nav_acompanhamento) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, new AcompanhamentoFragment()).commit();
        } else if (id == R.id.nav_website) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.food4fit.com.br/")));
            drawer.closeDrawer(GravityCompat.START);
        }

        if (item.isCheckable()) {
            configureToolbar(null);
            setTitle(item.getTitle());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(item.getTitle());
            }

            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void logout() {
        new AlertDialog.Builder(this)
            .setMessage("Você realmente deseja sair de sua conta?")
            .setCancelable(false)
            .setPositiveButton("Sim", (dialog, id) -> {
                Account[] accounts = accountManager.getAccountsByType("FOOD4FIT");
                if (accounts.length > 0) {
                    AppDatabase.getDatabase(MainActivity.this).getUsuarioDAO().logout(usuario.getId());
                    accountManager.removeAccount(accounts[0], null, null);
                    AlarmUtils.cancelAllAlarms(getApplicationContext(), new Intent(getApplicationContext(), AlarmReceiver.class));
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            })
            .setNegativeButton("Não", null)
            .show();
    }

    public void configureToolbar(Toolbar toolbar) {
        if (toolbar == null) {
            toolbar = findViewById(R.id.toolbar);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().show();
    }
}
