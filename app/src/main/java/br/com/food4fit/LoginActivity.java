package br.com.food4fit;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.food4fit.config.AppDatabase;
import br.com.food4fit.config.RetrofitConfig;
import br.com.food4fit.food4fit.R;
import br.com.food4fit.model.LoginModel;
import br.com.food4fit.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private AccountManager accountManager;
    private TextInputLayout tilEmail, tilSenha;
    private TextInputEditText edtEmail, edtSenha;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    private final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Food4fitApp.isDarkMode(this)) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }

        setContentView(R.layout.activity_login);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode("AIzaSyC2W_-v5j2w1Gfua-s2g5maaiC8VRIen6Y")
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        try {
            PackageInfo info = getPackageManager().getPackageInfo("br.com.food4fit.food4fit", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }


        LoginButton loginButton = new LoginButton(this);
        loginButton.setReadPermissions("email");

        Button btnFacebook = findViewById(R.id.btn_login_facebook);
        btnFacebook.setOnClickListener(view -> loginButton.performClick());

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("teste", "sucelso");
                Log.d("teste", loginResult.getAccessToken().getUserId());
                Log.d("teste", loginResult.getAccessToken().getToken());
                Log.d("teste", Profile.getCurrentProfile().getName());
                Log.d("teste", Profile.getCurrentProfile().getId());
                Log.d("teste", Profile.getCurrentProfile().getLinkUri().toString());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("teste", response.toString());
                            }
                    });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("teste", "cancelso");
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
                Log.d("teste", "errornelso");
            }
        });

        accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType("FOOD4FIT");
        if (accounts.length > 0) {
            Account account = accounts[0];
            Usuario usuario = AppDatabase.getDatabase(LoginActivity.this).getUsuarioDAO().findByEmail(account.name);
            if (usuario != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
            }
        }

        tilEmail = findViewById(R.id.til_login_email);
        edtEmail = findViewById(R.id.edt_login_email);
        tilSenha = findViewById(R.id.til_login_senha);
        edtSenha = findViewById(R.id.edt_login_senha);
        TextView txtEsqueciSenha = findViewById(R.id.txt_esqueci_senha);
        txtEsqueciSenha.setOnClickListener(view -> startActivity(new Intent(this, RecuperarSenhaActivity.class)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();

                String personName = acct.getDisplayName();
                String email = acct.getEmail();

                Log.d("teste", personName+"");
                Log.d("teste", email+"");
                Log.d("teste", acct.getId()+"");
                Log.d("teste", acct.getIdToken() + "");
                Log.d("teste", acct.getServerAuthCode() + "");
            } else {
                Log.d("teste", "fffff" + result.getStatus().toString());
            }
        }
    }

    private void setAccount(String email, String password, String authToken) {
        Account account = new Account(email, "FOOD4FIT");
        accountManager.addAccountExplicitly(account, password, null);
        accountManager.setAuthToken(account, "full_access", authToken);
    }

    public void login(View view) {
        final String email = edtEmail.getText().toString();
        final String senha = edtSenha.getText().toString();
        tilEmail.setError("");
        tilSenha.setError("");
        if (email.isEmpty()) {
            tilEmail.setError("Preencha um email");
            edtEmail.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Este email não é válido");
            edtEmail.requestFocus();
        } else if (senha.isEmpty()) {
            tilSenha.setError("Preencha uma senha");
            edtSenha.requestFocus();
        } else {
            if (((Food4fitApp) getApplication()).isNetworkAvailable()) {
                LoginModel model = new LoginModel(email, senha);
                Call<Usuario> call = new RetrofitConfig().getUsuarioService().login(model);
                call.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (response.code() == 401) {
                            tilSenha.setError("Email ou senha incorretos.");
                            edtSenha.setText("");
                        } else {
                            Usuario usuario = response.body();
                            if (usuario != null) {
                                setAccount(usuario.getEmail(), senha, usuario.getHash());
                                AppDatabase.getDatabase(LoginActivity.this).getUsuarioDAO().insert(usuario);
                                AppDatabase.getDatabase(LoginActivity.this).getUsuarioDAO().login(usuario.getId());

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("usuario", usuario);
                                startActivity(intent);
                                finish();
                            } else {
                                tilSenha.setError("Email ou senha incorretos.");
                                edtSenha.setText("");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        tilSenha.setError("Erro na comunicação com o servidor");
                        edtSenha.setText("");
                        t.printStackTrace();
                    }
                });

            } else {
                Snackbar.make(findViewById(android.R.id.content), "Você precisa estar conectado à internet para realizar login", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
