package br.com.food4fit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import br.com.food4fit.broadcast.AlarmReceiver;
import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.Alimento;
import br.com.food4fit.model.Dieta;
import br.com.food4fit.model.Refeicao;
import br.com.food4fit.model.Usuario;
import br.com.food4fit.util.AlarmUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AccountManager accountManager;
    private DrawerLayout drawer;
    private Usuario usuario;
    private CircleImageView imgAvatar;
    private TextView txtDrawerNome, txtDrawerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Food4fitApp.isDarkMode(this)) {
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

        ((Food4fitApp) getApplication()).sincronizarCompras(this);

        accountManager = AccountManager.get(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (navigationView.getMenu().size() > 0) {
            if (savedInstanceState == null) {
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
            } else if (getSupportFragmentManager().findFragmentById(R.id.layout_main) instanceof ConfiguracoesFragment) {
                configureToolbar(null);
                setTitle("Configurações");
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Configurações");
                }

                drawer.closeDrawer(GravityCompat.START);
            } else {
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
            }
        }

        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.img_drawer_avatar);
        txtDrawerNome = navigationView.getHeaderView(0).findViewById(R.id.txt_drawer_nome);
        txtDrawerEmail = navigationView.getHeaderView(0).findViewById(R.id.txt_drawer_email);
        atualizarHeader();

        ((Food4fitApp) getApplication()).sincronizarImagens();

        if (getIntent() != null) {
            importarDieta();
        }
    }

    public void atualizarHeader() {
        usuario = ((Food4fitApp) getApplication()).getUsuario();
        txtDrawerNome.setText(String.format(Food4fitApp.LOCALE, "%s %s", usuario.getNome(), usuario.getSobrenome()));
        txtDrawerEmail.setText(usuario.getEmail());
        if (usuario.getAvatar() != null && !usuario.getAvatar().isEmpty()) {
            Picasso.get().load("http://10.0.2.2/food4fit/" + usuario.getAvatar()).error(R.drawable.baseline_person_24).into(imgAvatar);
        }
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
        } else if (id == R.id.nav_hidratacao) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, new HidratacaoFragment()).commit();
        } else if (id == R.id.nav_compras) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, new ComprasFragment()).commit();
        } else if (id == R.id.nav_website) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.food4fit.com.br/")));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_configuracoes) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, new ConfiguracoesFragment()).commit();
        } else if (id == R.id.nav_perfil) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, new PerfilFragment()).commit();
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

    @SuppressWarnings("deprecation")
    private void logout() {
        new AlertDialog.Builder(this,
                Food4fitApp.isDarkMode(this) ? R.style.Theme_AppCompat_Dialog_Alert : R.style.Theme_AppCompat_Light_Dialog_Alert)
            .setMessage("Você realmente deseja sair de sua conta?")
            .setCancelable(false)
            .setPositiveButton("Sim", (dialog, id) -> {
                Account[] accounts = accountManager.getAccountsByType("FOOD4FIT");
                if (accounts.length > 0) {
                    AppDatabase.getDatabase(MainActivity.this).getUsuarioDAO().logout(usuario.getId());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        accountManager.removeAccount(accounts[0], null, null, null);
                    } else {
                        accountManager.removeAccount(accounts[0], null, null);
                    }

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

    private void importarDieta() {
        if (getIntent() != null && getIntent().getDataString() != null) {
            String data = getIntent().getDataString();
            if (!data.isEmpty() && data.startsWith("{")) {
                try {
                    Dieta dieta = new ObjectMapper().readValue(data, Dieta.class);
                    if (dieta != null) {
                        dieta.getData().setId(0);
                        dieta.getData().setIdUsuario(usuario.getId());
                        dieta.getData().setAtiva(true);
                        long idDieta = AppDatabase.getDatabase(this).getDietaDAO().insert(dieta.getData());

                        for (Refeicao refeicao : dieta.getRefeicoes()) {
                            refeicao.getData().setId(0);
                            refeicao.getData().setIdDieta((int) idDieta);
                            long idRefeicao = AppDatabase.getDatabase(this).getRefeicaoDAO().insert(refeicao.getData());

                            for (Alimento alimento : refeicao.getAlimentos()) {
                                alimento.setId(0);
                                alimento.setIdRefeicao((int) idRefeicao);
                                AppDatabase.getDatabase(this).getAlimentoDAO().insert(alimento);
                            }
                        }
                    }

                    Toast.makeText(this, "Dieta importada com sucesso", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
