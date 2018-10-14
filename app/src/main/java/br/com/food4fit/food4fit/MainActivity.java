package br.com.food4fit.food4fit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.food4fit.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.model.Usuario;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AccountManager accountManager;
    private DrawerLayout drawer;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null) {
            usuario = (Usuario) intent.getSerializableExtra("usuario");
        }

        if (intent == null || usuario == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        accountManager = AccountManager.get(this);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (navigationView.getMenu().size() > 0) {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }

        TextView txtDrawerNome = navigationView.getHeaderView(0).findViewById(R.id.txt_drawer_nome);
        TextView txtDrawerEmail = navigationView.getHeaderView(0).findViewById(R.id.txt_drawer_email);
        txtDrawerNome.setText(usuario.getNome() + " " + usuario.getSobrenome());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_sair) {
            logout();
        } else if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, new HomeFragment()).commit();
        } else if (id == R.id.nav_dietas) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, new DietasFragment()).commit();
        }

        if (item.isCheckable()) {
            setTitle(item.getTitle());
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
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            })
            .setNegativeButton("Não", null)
            .show();
    }
}
